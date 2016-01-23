package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.State
import rx.Subscription
import rx.subscriptions.Subscriptions

data class ApplicationState(
        val isFetching: Boolean = true,
        val gifs: List<Gif> = listOf(),
        val pagination: NextPage = NextPage(),
        val currentRequest: Subscription = Subscriptions.empty(),
        val activeGif: ActiveGif = ActiveGif()) : State
