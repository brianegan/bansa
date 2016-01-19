package com.brianegan.rxredux.listOfCounters

import com.brianegan.RxRedux.State
import com.github.andrewoma.dexx.kollection.ImmutableList
import com.github.andrewoma.dexx.kollection.immutableListOf
import java.util.*

data class ApplicationState(val counters: ImmutableList<Counter> = immutableListOf()) : State

data class Counter(val id: UUID = UUID.randomUUID(), val value: Int = 0)
