package com.brianegan.bansa.counter

import trikita.anvil.DSL.scrollView

internal fun counterScreen(model: RootView.CounterViewModel) {
    scrollView {
        counterView(model)
    }
}
