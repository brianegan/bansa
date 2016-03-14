package com.brianegan.bansa.randomGif

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.bansa.Store
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class RootActivity : AppCompatActivity() {
    val store = Injekt.get<Store<ApplicationState, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(RootView(this, store)) // Set the root view
    }
}
