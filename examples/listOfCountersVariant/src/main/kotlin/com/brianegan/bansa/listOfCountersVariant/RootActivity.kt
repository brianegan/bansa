package com.brianegan.bansa.listOfCountersVariant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.bansa.Action
import com.brianegan.bansa.Store
import javax.inject.Inject

public open class RootActivity : AppCompatActivity() {
    @Inject lateinit var store: Store<ApplicationState, Action>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.getObjectGraph()?.inject(this)
        store.dispatch(INIT()) // Initialize the store
        setContentView(RootView(this, store)) // Set the root view
    }
}
