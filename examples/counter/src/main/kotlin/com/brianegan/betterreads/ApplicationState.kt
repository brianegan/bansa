package com.brianegan.betterreads

import com.brianegan.RxRedux.State

data class ApplicationState(val counter: Int = 0) : State
