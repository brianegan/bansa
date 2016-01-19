package com.brianegan.rxredux.randomGif

import com.brianegan.RxRedux.State

data class ApplicationState(val isFetching: Boolean = true, val videoUrl: String = "") : State
