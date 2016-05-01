package com.brianegan.bansaDevTools

import com.brianegan.bansa.Action

class DevToolsReducerTest {
    data class TestState(val message: String = "initial state")
    data class TestAction(val type: String = "unknown") : Action
    object HeyHey : Action
    object CallApi : Action
    object Fetching : Action
    object FetchComplete : Action
    object Around : Action



}
