package com.brianegan.bansa.counter

import com.brianegan.bansa.BansaStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}


val store = BansaStore(ApplicationState(), CounterReducer())
