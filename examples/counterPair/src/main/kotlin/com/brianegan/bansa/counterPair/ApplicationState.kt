package com.brianegan.bansa.counterPair

import java.util.*

data class ApplicationState(val counters: Map<UUID, Int> = linkedMapOf(
        Pair(UUID.randomUUID(), 0),
        Pair(UUID.randomUUID(), 0)))
