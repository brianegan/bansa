package com.brianegan.betterreads

import android.content.Context
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

public class RootView(c: Context, val store: Store<ApplicationState, Action>) : RenderableView(c) {
    override fun view() {
        linearLayout {
            size(FILL, WRAP)
            orientation(LinearLayout.VERTICAL)

            textView {
                id(R.id.counter_text)
                text("Counts: ${store.getState().counter.toString()}")
            }

            button {
                id(R.id.counter_increment)
                size(FILL, WRAP)
                padding(dip(5))
                text("+")
                onClick({
                    store.dispatch(CounterActions.INCREMENT)
                })
            }

            button {
                id(R.id.counter_decrement)
                size(FILL, WRAP)
                padding(dip(5))
                text("-")
                onClick({
                    store.dispatch(CounterActions.DECREMENT)
                })
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
}
