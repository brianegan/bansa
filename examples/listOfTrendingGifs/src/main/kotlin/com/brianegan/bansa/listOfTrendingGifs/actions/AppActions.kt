package com.brianegan.bansa.listOfTrendingGifs.actions

import com.brianegan.bansa.listOfTrendingGifs.models.TrendingGifs

interface AppAction

object INIT : AppAction
object REFRESH : AppAction
object REFRESH_STARTED : AppAction
data class REFRESH_COMPLETED(val payload: TrendingGifs) : AppAction
object FETCH_NEXT_PAGE : AppAction
object FETCH_NEXT_PAGE_STARTED : AppAction
data class FETCH_NEXT_PAGE_COMPLETED(val payload: TrendingGifs) : AppAction
data class ORIENTATION_CHANGE(val orientation: Int) : AppAction

