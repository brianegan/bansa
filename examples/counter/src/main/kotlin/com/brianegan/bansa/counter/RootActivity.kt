package com.brianegan.bansa.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.bansa.Store
import com.brianegan.bansa.createStore

open class RootActivity : AppCompatActivity() {
    val store: Store<ApplicationState, Any> = createStore(ApplicationState(), counterReducer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store.dispatch(CounterActions.INIT) // Initialize the store
        setContentView(RootView(this, store))
    }
}
