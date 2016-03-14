package com.brianegan.bansa.listOfCounters

import java.util.*

data class ApplicationState(val counters: List<Counter> = listOf())

data class Counter(val id: UUID = UUID.randomUUID(), val value: Int = 0)
