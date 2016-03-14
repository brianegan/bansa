package com.brianegan.bansa.counterPair

import java.util.*

interface CounterAction

data class INIT(val initialState: ApplicationState = ApplicationState()) : CounterAction
data class INCREMENT(val id : UUID) : CounterAction
data class DECREMENT(val id : UUID) : CounterAction
