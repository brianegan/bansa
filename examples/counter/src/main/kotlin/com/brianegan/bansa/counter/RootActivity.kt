package com.brianegan.bansa.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.bansa.createStore

open class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val store = createStore(ApplicationState(), counterReducer)
        store.dispatch(CounterActions.INIT) // Initialize the store
        setContentView(RootView(this, store))
    }
}
