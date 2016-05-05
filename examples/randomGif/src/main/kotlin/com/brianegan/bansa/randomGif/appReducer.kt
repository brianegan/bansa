package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Reducer

val applicationReducer = Reducer<ApplicationState> { state, action ->
    when (action) {
        is INIT -> ApplicationState()
        is FETCHING -> state.copy(isFetching = true)
        is NEW_RANDOM_GIF -> state.copy(isFetching = false, videoUrl = action.videoUrl)
        else -> state
    }
}
