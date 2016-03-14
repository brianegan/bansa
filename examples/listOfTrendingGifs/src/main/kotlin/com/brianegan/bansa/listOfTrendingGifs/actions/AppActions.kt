package com.brianegan.bansa.listOfTrendingGifs.actions

import com.brianegan.bansa.listOfTrendingGifs.models.TrendingGifs
import rx.Subscription

interface AppAction

object INIT : AppAction
object REFRESH : AppAction
data class REFRESH_STARTED(val subscription: Subscription) : AppAction
data class REFRESH_COMPLETED(val payload: TrendingGifs) : AppAction
object FETCH_NEXT_PAGE : AppAction
data class FETCH_NEXT_PAGE_STARTED(val subscription: Subscription) : AppAction
data class FETCH_NEXT_PAGE_COMPLETED(val payload: TrendingGifs) : AppAction

