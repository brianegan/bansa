package com.brianegan.bansa.counterPair

import com.brianegan.bansa.Reducer
import java.util.*

class CounterReducer : Reducer<ApplicationState, CounterAction> {
    override fun reduce(state: ApplicationState, action: CounterAction): ApplicationState {
        when (action) {
            is INIT -> return action.initialState
            is INCREMENT -> {
                val counters = LinkedHashMap(state.counters)
                counters[action.id] = counters[action.id]?.plus(1)
                return state.copy(counters = counters)
            }
            is DECREMENT -> {
                val counters = LinkedHashMap(state.counters)
                counters[action.id] = counters[action.id]?.minus(1)
                return state.copy(counters = counters)
            }
            else -> return state
        }
    }
}
