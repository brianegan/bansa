package com.brianegan.bansa.counter

import android.content.Context
import android.view.View
import com.brianegan.bansa.Store
import com.brianegan.bansa.Subscription
import trikita.anvil.Anvil

class RootView(c: Context, val store: Store<ApplicationState>) : RenderableDrawerLayout(c) {

    override fun view() {
        counterScreen(buildPresentationModel())
    }

    val increment = View.OnClickListener {
        store.dispatch(CounterActions.INCREMENT)
    }

    val decrement = View.OnClickListener {
        store.dispatch(CounterActions.DECREMENT)
    }

    private fun buildPresentationModel(): CounterViewModel {
        val counter = store.state.counter

        return CounterViewModel(counter, increment, decrement)
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

    data class CounterViewModel(val counter: Int, val increment: OnClickListener, val decrement: OnClickListener)
}
