package com.brianegan.bansa.listOfCountersVariant

import com.brianegan.bansa.Action
import com.brianegan.bansa.Reducer
import com.github.andrewoma.dexx.kollection.toImmutableList

class CounterReducer : Reducer<ApplicationState> {
    override fun reduce(state: ApplicationState, action: Action): ApplicationState {
        when (action) {
            is INIT -> return action.state
            is ADD -> {
                return state.copy(counters = state.counters.plus(action.counter))
            }
            is REMOVE -> {
                val updatedCounters = state
                        .counters
                        .filter({ it.id.equals(action.id).not() })
                        .toImmutableList()

                return state.copy(counters = updatedCounters)
            }
            is INCREMENT -> {
                return state.copy(counters = state.counters.map({ counter: Counter ->
                    if (counter.id.equals(action.id)) {
                        counter.copy(value = counter.value.plus(1))
                    } else {
                        counter
                    }
                }).toImmutableList())
            }
            is DECREMENT -> {
                return state.copy(counters = state.counters.map({ counter ->
                    if (counter.id.equals(action.id)) {
                        counter.copy(value = counter.value.minus(1))
                    } else {
                        counter
                    }
                }).toImmutableList())
            }
            else -> return state
        }
    }
}
