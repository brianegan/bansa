package com.brianegan.bansa.listOfTrendingGifs

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.bansa.listOfTrendingDGifs.store
import com.brianegan.bansa.listOfTrendingGifs.actions.ORIENTATION_CHANGE

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(RootView(this, store))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig != null) {
            store.dispatch(ORIENTATION_CHANGE(newConfig.orientation))
        }
    }
}
