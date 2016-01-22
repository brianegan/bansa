package com.brianegan.bansa.listOfTrendingGifs

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
        store.dispatch(INIT)
    }

    companion object {
        private var instance: Application? = null

        fun getObjectGraph(): ObjectGraph? {
            return instance?.objectGraph
        }
    }
}
