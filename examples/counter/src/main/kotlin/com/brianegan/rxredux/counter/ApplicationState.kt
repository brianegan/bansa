package com.brianegan.rxredux.counter

import com.brianegan.RxRedux.State

data class ApplicationState(val counter: Int = 0) : State
