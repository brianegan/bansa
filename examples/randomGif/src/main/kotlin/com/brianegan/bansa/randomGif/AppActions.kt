package com.brianegan.bansa.randomGif

interface AppAction

object INIT : AppAction
object FETCHING : AppAction
object FETCH_RANDOM_GIF : AppAction
data class NEW_RANDOM_GIF(val videoUrl: String) : AppAction
