package com.brianegan.bansa.counter

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brianegan.bansa.Store
import rx.Subscription
import rx.subscriptions.Subscriptions
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class RootView(c: Context, val store: Store<ApplicationState, Any>) : RenderableView(c) {
    override fun view() {
        template(buildPresentationModel())
    }

    val increment = View.OnClickListener {
        store.dispatch(CounterActions.INCREMENT)
    }

    val decrement = View.OnClickListener {
        store.dispatch(CounterActions.DECREMENT)
    }

    private fun buildPresentationModel(): ViewModel {
        val counter = store.state.counter

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

        subscription = store.stateChanges.subscribe {
            Anvil.render()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription.unsubscribe()
    }

    data class ViewModel(val counter: Int, val increment: OnClickListener, val decrement: OnClickListener)
}
