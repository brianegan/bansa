package com.brianegan.bansa.todoList

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        store.dispatch(INIT()) // Initialize the store
    }
}
