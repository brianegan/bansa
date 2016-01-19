package com.brianegan.rxredux.listOfCountersVariant

import com.github.andrewoma.dexx.kollection.toImmutableList

val counterReducer = { state: ApplicationState, action: CounterAction ->
    when (action) {
        is INIT -> action.state
        is ADD -> {
            state.copy(counters = state.counters.plus(action.counter))
        }
        is REMOVE -> {
            val updatedCounters = state
                    .counters
                    .filter({ it.id.equals(action.id).not() })
                    .toImmutableList()

            state.copy(counters = updatedCounters)
        }
        is INCREMENT -> {
            state.copy(counters = state.counters.map({ counter: Counter ->
                if (counter.id.equals(action.id)) {
                    counter.copy(value = counter.value.plus(1))
                } else {
                    counter
                }
            }).toImmutableList())
        }
        is DECREMENT -> {
            state.copy(counters = state.counters.map({ counter ->
                if (counter.id.equals(action.id)) {
                    counter.copy(value = counter.value.minus(1))
                } else {
                    counter
                }
            }).toImmutableList())
        }
        else -> state
    }
}
