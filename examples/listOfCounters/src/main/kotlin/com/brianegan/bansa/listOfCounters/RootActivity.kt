package com.brianegan.bansa.listOfCounters

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

open class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(RootView(this, store))
    }
}
