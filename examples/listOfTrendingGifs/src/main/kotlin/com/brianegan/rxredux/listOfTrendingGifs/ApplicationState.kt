package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.State
import rx.Subscription
import rx.subscriptions.Subscriptions

data class ApplicationState(val isFetching: Boolean = true,
                            val gifs: List<Gif> = listOf(),
                            val pagination: NextPage = NextPage(),
                            val currentRequest: Subscription = Subscriptions.empty()) : State

data class Gif(val url: String = "", val width: Int = 0, val height: Int = 0)
