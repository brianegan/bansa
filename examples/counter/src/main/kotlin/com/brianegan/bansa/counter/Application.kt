package com.brianegan.bansa.counter

import com.brianegan.bansaDevTools.DevToolsStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}


val store = DevToolsStore(ApplicationState(), CounterReducer())
