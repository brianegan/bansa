package com.brianegan.listOfCounters

import com.brianegan.RxRedux.Action
import java.util.*

interface CounterAction : Action {}

object INIT : CounterAction
data class INCREMENT(val id : UUID) : CounterAction
data class DECREMENT(val id : UUID) : CounterAction
