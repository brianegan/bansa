package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.Action
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

        is PLAY_GIF -> state.copy(
                activeGif = ActiveGif(
                        id = action.id,
                        isPlaying = false,
                        isFetching = true))

        is STOP_GIF -> state.copy(
                activeGif = ActiveGif(
                        id = "",
                        isPlaying = false,
                        isFetching = false))

        is VIDEO_STARTED -> state.copy(
                activeGif = state.activeGif.copy(
                        isPlaying = true,
                        isFetching = false
                )
        )

        else -> state
    }
}
