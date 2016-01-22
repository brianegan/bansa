package com.brianegan.rxredux.counter

import com.brianegan.RxRedux.createStore
import rx.schedulers.Schedulers

val counterReducer = { state: ApplicationState, action: CounterAction ->
    createStore(
            ApplicationState(),
            reducer
    );

    when (action) {
        is CounterActions.INIT -> state
        is CounterActions.INCREMENT -> state.copy(counter = state.counter.plus(1))
        is CounterActions.DECREMENT -> state.copy(counter = state.counter.minus(1))
        else -> state
    }
}

