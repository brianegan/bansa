package com.brianegan.betterreads

import com.brianegan.RxRedux.State
import java.util.*

data class ApplicationState(val counters: Map<UUID, Int> = hashMapOf(
        Pair(UUID.randomUUID(), 0),
        Pair(UUID.randomUUID(), 0))) : State
