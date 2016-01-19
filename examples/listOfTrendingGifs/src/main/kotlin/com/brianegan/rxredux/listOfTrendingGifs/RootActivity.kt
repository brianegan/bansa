package com.brianegan.rxredux.listOfTrendingGifs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import javax.inject.Inject

public open class RootActivity : AppCompatActivity() {
    @Inject lateinit var store: Store<ApplicationState, Action>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.getObjectGraph()?.inject(this)
        setContentView(RootView(this, store)) // Set the root view
    }
}
