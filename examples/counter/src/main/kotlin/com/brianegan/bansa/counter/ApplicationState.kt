package com.brianegan.bansa.counter

import com.brianegan.bansa.State

data class ApplicationState(val counter: Int = 0) : State
