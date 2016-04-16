package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.createStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT())
    }
}

val store = createStore(ApplicationState(), applicationReducer)
