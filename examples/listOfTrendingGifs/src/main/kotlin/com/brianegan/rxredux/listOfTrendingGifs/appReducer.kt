package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.Action
import rx.subscriptions.Subscriptions

val applicationReducer = { state: ApplicationState, action: Action ->
    when (action) {
        is INIT -> ApplicationState()

        is REFRESH_STARTED -> state.copy(
                isFetching = true,
                gifs = listOf(),
                currentRequest = action.subscription)

        is FETCH_COMPLETED -> state.copy(
                isFetching = false,
                gifs = action.gifs.gifs,
                currentRequest = Subscriptions.empty())

        is FETCH_NEXT_PAGE_STARTED -> state.copy(
                isFetching = true,
                currentRequest = action.subscription)

        else -> state
    }
}
