package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Action
import com.brianegan.bansa.Store
import dagger.ObjectGraph
import javax.inject.Inject

class Application : android.app.Application() {
    @Inject lateinit var store: Store<ApplicationState, Action>
    var objectGraph: ObjectGraph? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        objectGraph = ObjectGraph.create(ApplicationModule(this), StoreModule())
        getObjectGraph()?.inject(this)

        // Initialize the store and immediately fetch a random gif!
        store.dispatch(INIT)
        store.dispatch(FETCH_RANDOM_GIF)
    }

    companion object {
        private var instance: Application? = null

        fun getObjectGraph(): ObjectGraph? {
            return instance?.objectGraph
        }
    }
}
