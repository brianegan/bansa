package com.brianegan.bansa.randomGif

val applicationReducer = { state: ApplicationState, action: Any ->
    when (action) {
        is INIT -> ApplicationState()
        is FETCHING -> state.copy(isFetching = true)
        is NEW_RANDOM_GIF -> state.copy(isFetching = false, videoUrl = action.videoUrl)
        else -> state
    }
}
