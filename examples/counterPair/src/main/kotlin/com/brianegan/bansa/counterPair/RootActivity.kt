package com.brianegan.bansa.counterPair

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(RootView(this, store))
    }
}
