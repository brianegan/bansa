package com.brianegan.listOfCounters

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.subscriptions.Subscriptions
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import java.util.*

public class RootView(c: Context, val store: Store<ApplicationState, Action>) : RenderableView(c) {
    override fun view() {
        linearLayout {
            orientation(LinearLayout.VERTICAL)

            store.getState().counters.keys.forEach { id ->
                template(buildPresentationModel(id))
            }
        }
    }

    private fun buildPresentationModel(id: UUID): ViewModel {
        val counter = store.getState().counters[id]!!
        val increment = View.OnClickListener {
            store.dispatch(INCREMENT(id))
        }

        val decrement = View.OnClickListener {
            store.dispatch(DECREMENT(id))
        }

        return ViewModel(counter, increment, decrement)
    }

    private fun template(model: ViewModel) {
        val (counter, increment, decrement) = model

        linearLayout {
            size(FILL, WRAP)
            orientation(LinearLayout.VERTICAL)

            textView {
                id(R.id.counter_text)
                text("Counts: ${counter.toString()}")
            }

            button {
                id(R.id.counter_increment)
                size(FILL, WRAP)
                padding(dip(10))
                text("+")
                onClick(increment)
            }

            button {
                id(R.id.counter_decrement)
                size(FILL, WRAP)
                padding(dip(5))
                text("-")
                onClick(decrement)
            }
        }
    }

    var subscription: Subscription = Subscriptions.empty()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        subscription = store.state.observeOn(AndroidSchedulers.mainThread()).subscribe(Action1 {
            Anvil.render()
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription.unsubscribe()
    }

    data class ViewModel(val counter: Int, val increment: View.OnClickListener, val decrement: View.OnClickListener)
}
