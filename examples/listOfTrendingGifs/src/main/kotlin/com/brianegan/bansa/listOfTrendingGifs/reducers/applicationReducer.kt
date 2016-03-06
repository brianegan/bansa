package com.brianegan.bansa.listOfTrendingGifs.reducers

import com.brianegan.bansa.Action
import com.brianegan.bansa.listOfTrendingGifs.actions.*
import com.brianegan.bansa.listOfTrendingGifs.api.NextPage
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import rx.subscriptions.Subscriptions

val applicationReducer = { state: ApplicationState, action: Action ->
    when (action) {
        is INIT -> ApplicationState()

        is REFRESH_STARTED -> state.copy(
                isRefreshing = true,
                currentRequest = action.subscription)

        is REFRESH_COMPLETED -> state.copy(
                isRefreshing = false,
                gifs = action.payload.gifs,
                pagination = NextPage(
                        offset = action.payload.pagination.offset.plus(action.payload.pagination.count)),
                currentRequest = Subscriptions.empty())

        is FETCH_NEXT_PAGE_STARTED -> state.copy(isFetching = true)

        is FETCH_NEXT_PAGE_COMPLETED -> state.copy(
                isFetching = false,
                gifs = state.gifs.plus(action.payload.gifs),
                pagination = NextPage(
                        offset = action.payload.pagination.offset.plus(action.payload.pagination.count)),
                currentRequest = Subscriptions.empty())

        else -> state
    }
}
