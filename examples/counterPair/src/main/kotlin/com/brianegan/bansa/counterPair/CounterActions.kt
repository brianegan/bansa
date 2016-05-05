package com.brianegan.bansa.counterPair

import com.brianegan.bansa.Action
import java.util.*

data class INIT(val initialState: ApplicationState = ApplicationState()) : Action
data class INCREMENT(val id : UUID) : Action
data class DECREMENT(val id : UUID) : Action
