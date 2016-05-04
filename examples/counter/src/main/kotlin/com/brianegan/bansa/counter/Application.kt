package com.brianegan.bansa.counter

import store

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}
