package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.State
import com.brianegan.bansa.listOfTrendingGifs.api.NextPage
import rx.Subscription
import rx.subscriptions.Subscriptions

data class ApplicationState(val isFetching: Boolean = true,
                            val gifs: List<Gif> = listOf(),
                            val pagination: NextPage = NextPage(),
                            val currentRequest: Subscription = Subscriptions.empty()) : State
