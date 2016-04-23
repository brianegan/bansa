package com.brianegan.bansa.counterPair

import com.brianegan.bansa.BaseStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT()) // Initialize the store
    }
}

val store = BaseStore(ApplicationState(), ApplicationReducer())
