package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.State
import okhttp3.Request
import rx.Subscription
import rx.subscriptions.Subscriptions

data class ApplicationState(val isFetching: Boolean = true,
                            val gifs: List<Gif> = listOf(),
                            val currentRequest: Subscription = Subscriptions.empty(),
                            val nextPageRequest: Request = Request.Builder().url("http://www.google.com").build()) : State

data class Gif(val videoUrl: String = "", val width: Int = 0, val height: Int = 0)
