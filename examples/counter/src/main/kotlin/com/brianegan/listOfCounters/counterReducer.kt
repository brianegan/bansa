package com.brianegan.listOfCounters

val counterReducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is CounterActions.INIT -> state
        is CounterActions.INCREMENT -> state.copy(counter = state.counter + 1)
        is CounterActions.DECREMENT -> state.copy(counter = Math.max(0, state.counter - 1))
        else -> state
    }
}
