package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.BansaStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT())
    }
}

val store = BansaStore(ApplicationState(), CounterReducer())
