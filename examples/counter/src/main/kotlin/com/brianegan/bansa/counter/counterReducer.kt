package com.brianegan.bansa.counter

val counterReducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is CounterActions.INIT -> state
        is CounterActions.INCREMENT -> state.copy(counter = state.counter.plus(1))
        is CounterActions.DECREMENT -> state.copy(counter = state.counter.minus(1))
        else -> state
    }
}

