package com.brianegan.bansa.listOfCounters

import android.view.View
import android.widget.LinearLayout
import com.brianegan.bansa.Store
import trikita.anvil.DSL.*

fun counterView(model: CounterViewModel) {
    val (counter, increment, decrement) = model

    linearLayout {
        size(FILL, WRAP)
        orientation(LinearLayout.HORIZONTAL)
        margin(0, dip(10))

        textView {
            size(0, WRAP)
            weight(1f)
            text("Counts: ${counter.toString()}")
        }

        button {
            size(WRAP, WRAP)
            padding(dip(10))
            text("+")
            onClick(increment)
        }

        button {
            size(WRAP, WRAP)
            padding(dip(5))
            text("-")
            onClick(decrement)
        }
    }
}

fun buildMapCounterToCounterViewModel(store: Store<ApplicationState, Any>): (Counter) -> CounterViewModel {
    return { counter ->
        val (id, value) = counter
        val increment = View.OnClickListener {
            store.dispatch(INCREMENT(id))
        }
        val decrement = View.OnClickListener { view ->
            store.dispatch(DECREMENT(id))
        }

        CounterViewModel(value, increment, decrement)
    }
}

data class CounterViewModel(val counter: Int, val increment: View.OnClickListener, val decrement: View.OnClickListener)
