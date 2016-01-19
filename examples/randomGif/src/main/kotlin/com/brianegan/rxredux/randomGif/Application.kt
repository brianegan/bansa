package com.brianegan.rxredux.randomGif

import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import dagger.ObjectGraph
import javax.inject.Inject

class Application : android.app.Application() {
    @Inject lateinit var store: Store<ApplicationState, Action>
    var objectGraph: ObjectGraph? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        objectGraph = ObjectGraph.create(ApplicationModule(this), ReduxModule())
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
