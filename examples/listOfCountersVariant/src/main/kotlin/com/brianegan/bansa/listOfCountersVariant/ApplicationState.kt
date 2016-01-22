package com.brianegan.bansa.listOfCountersVariant

import com.brianegan.bansa.State
import java.util.*

data class ApplicationState(val counters: List<Counter> = listOf()) : State

data class Counter(val id: UUID = UUID.randomUUID(), val value: Int = 0)
