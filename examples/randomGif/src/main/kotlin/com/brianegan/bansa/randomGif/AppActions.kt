package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Action

interface AppAction : Action {}

object INIT : AppAction
object FETCHING : AppAction
object FETCH_RANDOM_GIF : AppAction
data class NEW_RANDOM_GIF(val videoUrl: String) : AppAction
