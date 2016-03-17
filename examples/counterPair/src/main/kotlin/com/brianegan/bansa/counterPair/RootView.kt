package com.brianegan.bansa.counterPair

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brianegan.bansa.Store
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import java.util.*

class RootView(c: Context, val store: Store<ApplicationState, Any>) : RenderableView(c) {
    override fun view() {
        linearLayout {
            orientation(LinearLayout.VERTICAL)

            store.state.counters.keys.forEach { id ->
                template(buildPresentationModel(id))
            }
        }
    }

    private fun buildPresentationModel(id: UUID): ViewModel {
        val counter = store.state.counters[id]!!
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
                text("Counts: ${counter.toString()}")
            }

            button {
                size(FILL, WRAP)
                padding(dip(10))
                text("+")
                onClick(increment)
            }

            button {
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

        subscription = store.stateChanges.observeOn(AndroidSchedulers.mainThread()).subscribe {
            Anvil.render()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription.unsubscribe()
    }

    data class ViewModel(val counter: Int, val increment: View.OnClickListener, val decrement: View.OnClickListener)
}
