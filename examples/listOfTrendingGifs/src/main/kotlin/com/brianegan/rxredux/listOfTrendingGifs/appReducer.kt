package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.Action
import com.brianegan.rxredux.listOfTrendingGifs.api.NextPage
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
                gifs = state.gifs.plus(action.payload.gifs),
                pagination = NextPage(
                        offset = action.payload.pagination.offset
                                .plus(action.payload.pagination.count)),
                currentRequest = Subscriptions.empty())

        is FETCH_NEXT_PAGE_STARTED -> state.copy(isFetching = true)

        else -> state
    }
}
