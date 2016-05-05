package com.brianegan.bansaDevTools

import com.brianegan.bansa.Action
import com.brianegan.bansa.Reducer

class DevToolsTestReducer : Reducer<DevToolsReducerTest.TestState> {
    override fun reduce(state: DevToolsReducerTest.TestState, action: Action): DevToolsReducerTest.TestState {
        when (action) {
            is DevToolsReducerTest.TestAction -> {
                if (updated) {
                    return state.copy("updated ${action.message}")
                } else {
                    return state.copy(action.message)
                }
            }
            else -> return state
        }
    }

    var updated = false;
}
