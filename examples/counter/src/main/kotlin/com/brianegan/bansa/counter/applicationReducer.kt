package com.brianegan.bansa.counter

import com.brianegan.bansa.Action

val applicationReducer = { state: ApplicationState, action: Action ->
    when (action) {
        is CounterAction -> counterReducer(state, action)
        else -> state
    }
}
