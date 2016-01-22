package com.brianegan.bansa.randomGif

import com.brianegan.bansa.State

data class ApplicationState(val isFetching: Boolean = true, val videoUrl: String = "") : State
