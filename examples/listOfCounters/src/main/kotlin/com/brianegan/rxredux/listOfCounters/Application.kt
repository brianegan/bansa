package com.brianegan.rxredux.listOfCounters

import com.brianegan.rxredux.listOfCounters.ApplicationModule
import com.brianegan.rxredux.listOfCounters.ReduxModule
import dagger.ObjectGraph

class Application : android.app.Application() {
    var objectGraph: ObjectGraph? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        objectGraph = ObjectGraph.create(ApplicationModule(this), ReduxModule())
    }

    companion object {
        private var instance: Application? = null

        fun getObjectGraph(): ObjectGraph? {
            return instance?.objectGraph
        }
    }
}
