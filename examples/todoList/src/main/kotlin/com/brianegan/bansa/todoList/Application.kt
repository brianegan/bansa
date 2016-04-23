package com.brianegan.bansa.todoList

import com.brianegan.bansa.BaseStore

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(INIT()) // Initialize the store
    }
}

val store = BaseStore(ApplicationState(), applicationReducer)
