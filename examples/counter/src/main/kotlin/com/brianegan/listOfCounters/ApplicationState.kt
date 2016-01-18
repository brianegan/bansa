package com.brianegan.listOfCounters

import com.brianegan.RxRedux.State

data class ApplicationState(val counter: Int = 0) : State
