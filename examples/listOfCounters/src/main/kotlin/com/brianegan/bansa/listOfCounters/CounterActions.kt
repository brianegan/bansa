package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.Action
import java.util.*

data class INIT(val state : ApplicationState = ApplicationState()) : Action
data class ADD(val counter : Counter = Counter()) : Action
object  REMOVE : Action
data class INCREMENT(val id : UUID) : Action
data class DECREMENT(val id : UUID) : Action
