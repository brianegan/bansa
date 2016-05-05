package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.Action
import com.brianegan.bansa.Reducer

class CounterReducer : Reducer<ApplicationState> {
    override fun reduce(state: ApplicationState, action: Action): ApplicationState {
        when (action) {
            is INIT -> return action.state
            is ADD -> {
                return state.copy(counters = state.counters.plus(action.counter))
            }
            is REMOVE -> {
                return state.copy(counters = state.counters.dropLast(1))
            }
            is INCREMENT -> {
                return state.copy(counters = state.counters.map({ counter ->
                    if (counter.id.equals(action.id)) {
                        counter.copy(value = counter.value + 1)
                    } else {
                        counter
                    }
                }))
            }
            is DECREMENT -> {
                return state.copy(counters = state.counters.map({ counter ->
                    if (counter.id.equals(action.id)) {
                        counter.copy(value = counter.value - 1)
                    } else {
                        counter
                    }
                }))
            }
            else -> return state
        }
    }
}
