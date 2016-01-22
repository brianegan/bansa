package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.State
import com.brianegan.rxredux.listOfTrendingGifs.api.NextPage
import rx.Subscription
import rx.subscriptions.Subscriptions

data class ApplicationState(val isFetching: Boolean = true,
                            val gifs: List<Gif> = listOf(),
                            val pagination: NextPage = NextPage(),
                            val currentRequest: Subscription = Subscriptions.empty()) : State
