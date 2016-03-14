package com.brianegan.bansa.listOfCountersVariant

import java.util.*

interface CounterAction

data class INIT(val state : ApplicationState = ApplicationState()) : CounterAction
data class ADD(val counter : Counter = Counter()) : CounterAction
data class REMOVE(val id : UUID) : CounterAction
data class INCREMENT(val id : UUID) : CounterAction
data class DECREMENT(val id : UUID) : CounterAction
