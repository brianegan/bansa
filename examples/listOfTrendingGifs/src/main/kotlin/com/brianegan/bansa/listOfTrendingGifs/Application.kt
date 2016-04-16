package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.BaseStore
import com.brianegan.bansa.listOfTrendingGifs.actions.INIT
import com.brianegan.bansa.listOfTrendingGifs.middleware.GifMiddleware
import com.brianegan.bansa.listOfTrendingGifs.middleware.LoggingMiddleware
import com.brianegan.bansa.listOfTrendingGifs.reducers.ApplicationReducer
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(INIT)
    }
}

val store = BaseStore.create(ApplicationState(), ApplicationReducer(), listOf(GifMiddleware(), LoggingMiddleware()))
