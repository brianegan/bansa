package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.applyMiddleware
import com.brianegan.bansa.createStore
import com.brianegan.bansa.listOfTrendingGifs.actions.INIT
import com.brianegan.bansa.listOfTrendingGifs.middleware.gifMiddleware
import com.brianegan.bansa.listOfTrendingGifs.middleware.loggingMiddleware
import com.brianegan.bansa.listOfTrendingGifs.reducers.applicationReducer
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(INIT)
    }
}

val store = applyMiddleware(gifMiddleware, loggingMiddleware)(createStore(ApplicationState(), applicationReducer))
