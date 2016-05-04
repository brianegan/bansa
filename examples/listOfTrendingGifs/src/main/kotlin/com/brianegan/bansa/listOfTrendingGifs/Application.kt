package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingDGifs.store
import com.brianegan.bansa.listOfTrendingGifs.actions.INIT
import com.brianegan.bansa.listOfTrendingGifs.middleware.GifMiddleware
import com.brianegan.bansa.listOfTrendingGifs.middleware.LoggingMiddleware
import com.brianegan.bansa.listOfTrendingGifs.reducers.ApplicationReducer
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansaDevTools.DevToolsStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(INIT)
    }
}


