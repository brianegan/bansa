package com.brianegan.bansa.counter

import android.widget.LinearLayout
import trikita.anvil.DSL.*

fun counterView(model: RootView.CounterViewModel) {
    val (counter, increment, decrement) = model

    linearLayout {
        size(FILL, WRAP)
        orientation(LinearLayout.VERTICAL)
        id(R.id.counter_view)

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
    }
}
