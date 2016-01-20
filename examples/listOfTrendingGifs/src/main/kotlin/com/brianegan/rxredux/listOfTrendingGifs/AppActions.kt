package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.Action
import rx.Subscription

interface AppAction : Action {}

object INIT : AppAction
object REFRESH : AppAction
object FETCH_NEXT_PAGE : AppAction
data class REFRESH_STARTED(val subscription: Subscription) : AppAction
data class FETCH_NEXT_PAGE_STARTED(val subscription: Subscription) : AppAction
data class FETCH_COMPLETED(val payload: TrendingGifs) : AppAction

