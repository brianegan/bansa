package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.listOfCounters.ApplicationModule
import com.brianegan.bansa.listOfCounters.StoreModule
import dagger.ObjectGraph

class Application : android.app.Application() {
    var objectGraph: ObjectGraph? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        objectGraph = ObjectGraph.create(ApplicationModule(this), StoreModule())
    }

    companion object {
        private var instance: Application? = null

        fun getObjectGraph(): ObjectGraph? {
            return instance?.objectGraph
        }
    }
}
