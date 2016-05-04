package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Action

object INIT : Action
object FETCHING : Action
object FETCH_RANDOM_GIF : Action
data class NEW_RANDOM_GIF(val videoUrl: String) : Action
