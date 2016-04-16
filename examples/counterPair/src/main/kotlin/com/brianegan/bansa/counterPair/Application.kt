package com.brianegan.bansa.counterPair

import com.brianegan.bansa.createStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT()) // Initialize the store
    }
}

val store = createStore(ApplicationState(), applicationReducer)
