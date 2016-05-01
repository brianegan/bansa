package com.brianegan.bansa.counter

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brianegan.bansa.Store
import com.brianegan.bansa.Subscription
import com.brianegan.bansaDevToolsUi.BansaDevToolsPresenter
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class RootView(c: Context, val store: Store<ApplicationState>) : RenderableView(c) {
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

        scrollView {
            linearLayout {
                size(FILL, WRAP)
                orientation(LinearLayout.VERTICAL)

                textView {
                    text(counter.toString())
                    gravity(CENTER_HORIZONTAL)
                    textSize(sip(100F));
                    padding(0, dip(40))

                }

                button {
                    size(FILL, WRAP)
                    padding(dip(10))
                    text("+")
                    onClick(increment)
                    margin(dip(12), 0)
                }

                button {
                    size(FILL, WRAP)
                    padding(dip(5))
                    text("-")
                    onClick(decrement)
                    margin(dip(12), 0)
                }

                xml(R.layout.bansa_dev_tools) {
                    init {
                        val presenter = BansaDevToolsPresenter<ApplicationState>(store)
                        presenter.bind(Anvil.currentView())
                    }
                }
            }
        }
    }

    var subscription: Subscription? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        subscription = store.subscribe {
            Anvil.render()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription?.unsubscribe()
    }

    data class ViewModel(val counter: Int, val increment: OnClickListener, val decrement: OnClickListener)
}
