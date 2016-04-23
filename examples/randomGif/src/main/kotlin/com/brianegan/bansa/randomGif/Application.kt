package com.brianegan.bansa.randomGif

import com.brianegan.bansa.BaseStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the store and immediately fetch a random gif!
        store.dispatch(INIT)
        store.dispatch(FETCH_RANDOM_GIF)
    }
}

val store = BaseStore(ApplicationState(), applicationReducer, gifMiddleware)
