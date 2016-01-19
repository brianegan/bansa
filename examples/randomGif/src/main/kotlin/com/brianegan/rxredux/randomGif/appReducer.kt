package com.brianegan.rxredux.randomGif

import com.brianegan.RxRedux.Action

val applicationReducer = { state: ApplicationState, action: Action ->
    when (action) {
        is INIT -> ApplicationState()
        is FETCHING -> state.copy(isFetching = true)
        is NEW_RANDOM_GIF -> state.copy(isFetching = false, videoUrl = action.videoUrl)
        else -> state
    }
}
