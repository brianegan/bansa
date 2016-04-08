package com.brianegan.bansa.listOfTrendingGifs

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.actions.ORIENTATION_CHANGE
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.fullType
import uy.kohesive.injekt.api.get

class RootActivity : AppCompatActivity() {
    val store = Injekt.get(fullType<Store<ApplicationState, Any>>())

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
