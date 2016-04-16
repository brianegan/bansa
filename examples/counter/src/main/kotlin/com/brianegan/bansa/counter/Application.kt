package com.brianegan.bansa.counter

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}
