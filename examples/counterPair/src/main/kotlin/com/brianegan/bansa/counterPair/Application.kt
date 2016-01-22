package com.brianegan.bansa.counterPair

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
