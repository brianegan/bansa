package com.brianegan.rxredux.counter

import com.brianegan.RxRedux.Action

val applicationReducer = { state: ApplicationState, action: Action ->
    when (action) {
        is CounterAction -> counterReducer(state, action)
        else -> state
    }
}
