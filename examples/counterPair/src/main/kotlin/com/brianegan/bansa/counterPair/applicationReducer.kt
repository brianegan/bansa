package com.brianegan.bansa.counterPair

import com.brianegan.bansa.Reducer

class ApplicationReducer(
        val counterReducer: CounterReducer = CounterReducer()) : Reducer<ApplicationState, Any> {
    override fun invoke(state: ApplicationState, action: Any): ApplicationState {
        when (action) {
            is CounterAction -> return counterReducer(state, action)
            else -> return state
        }
    }
}
