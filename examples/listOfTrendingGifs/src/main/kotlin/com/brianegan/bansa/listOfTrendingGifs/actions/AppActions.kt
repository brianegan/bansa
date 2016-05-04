package com.brianegan.bansa.listOfTrendingGifs.actions

import com.brianegan.bansa.Action
import com.brianegan.bansa.listOfTrendingGifs.models.TrendingGifs

object INIT : Action
object REFRESH : Action
object REFRESH_STARTED : Action
data class REFRESH_COMPLETED(val payload: TrendingGifs) : Action
object FETCH_NEXT_PAGE : Action
object FETCH_NEXT_PAGE_STARTED : Action
data class FETCH_NEXT_PAGE_COMPLETED(val payload: TrendingGifs) : Action
data class ORIENTATION_CHANGE(val orientation: Int) : Action

