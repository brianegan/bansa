package com.brianegan.bansa.listOfCountersVariant

val applicationReducer = { state: ApplicationState, action: Any ->
    when (action) {
        is CounterAction -> counterReducer(state, action)
        else -> state
    }
}
