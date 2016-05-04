package com.brianegan.bansa.listOfCountersVariant

import com.brianegan.bansa.Action
import java.util.*

data class INIT(val state : ApplicationState = ApplicationState()) : Action
data class ADD(val counter : Counter = Counter()) : Action
data class REMOVE(val id : UUID) : Action
data class INCREMENT(val id : UUID) : Action
data class DECREMENT(val id : UUID) : Action
