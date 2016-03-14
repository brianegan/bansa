package com.brianegan.bansa.listOfCounters

val applicationReducer = { state: ApplicationState, action: Any ->
    when (action) {
        is CounterAction -> counterReducer(state, action)
        else -> state
    }
}
