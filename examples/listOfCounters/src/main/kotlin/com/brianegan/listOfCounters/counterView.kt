package com.brianegan.listOfCounters

import android.view.View
import android.widget.LinearLayout
import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
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
            id(R.id.counter_text)
            text("Counts: ${counter.toString()}")
        }

        button {
            id(R.id.counter_increment)
            size(WRAP, WRAP)
            padding(dip(10))
            text("+")
            onClick(increment)
        }

        button {
            id(R.id.counter_decrement)
            size(WRAP, WRAP)
            padding(dip(5))
            text("-")
            onClick(decrement)
        }
    }
}

fun buildMapCounterToCounterViewModel(store: Store<ApplicationState, Action>): (Counter) -> CounterViewModel {
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
