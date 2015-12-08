package com.brianegan.betterreads

val counterReducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is CounterActions.INIT -> state
        is CounterActions.INCREMENT -> state.copy(counter = state.counter + 1)
        is CounterActions.DECREMENT -> state.copy(counter = state.counter - 1)
        else -> state
    }
}
