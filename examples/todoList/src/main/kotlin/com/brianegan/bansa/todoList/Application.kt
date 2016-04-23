package com.brianegan.bansa.todoList

import com.brianegan.bansa.BansaStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(INIT()) // Initialize the store
    }
}

val store = BansaStore(ApplicationState(), applicationReducer)
