package com.brianegan.rxredux.listOfCounters

import com.github.andrewoma.dexx.kollection.toImmutableList

val counterReducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is INIT -> action.state
        is ADD -> {
            state.copy(counters = state.counters.plus(action.counter))
        }
        is REMOVE -> {
            state.copy(counters = state.counters.dropLast(1))
        }
        is INCREMENT -> {
            state.copy(counters = state.counters.map({ counter ->
                if (counter.id.equals(action.id)) {
                    counter.copy(value = counter.value + 1)
                } else {
                    counter
                }
            }).toImmutableList())
        }
        is DECREMENT -> {
            state.copy(counters = state.counters.map({ counter ->
                if (counter.id.equals(action.id)) {
                    counter.copy(value = counter.value - 1)
                } else {
                    counter
                }
            }).toImmutableList())
        }
        else -> state
    }
}
