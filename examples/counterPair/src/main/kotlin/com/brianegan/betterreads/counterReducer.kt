package com.brianegan.betterreads

import java.util.*

val counterReducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is INIT -> ApplicationState()
        is INCREMENT -> {
            val counters = HashMap(state.counters)
            counters[action.id] = counters[action.id]?.plus(1)
            state.copy(counters = counters)
        }
        is DECREMENT -> {
            val counters = HashMap(state.counters)
            counters[action.id] = counters[action.id]?.minus(1)
            state.copy(counters = counters)
        }
        else -> state
    }
}
