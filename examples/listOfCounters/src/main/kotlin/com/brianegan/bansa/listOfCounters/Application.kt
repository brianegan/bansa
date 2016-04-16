package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.BaseStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT())
    }
}

val store = BaseStore.create(ApplicationState(), CounterReducer())
