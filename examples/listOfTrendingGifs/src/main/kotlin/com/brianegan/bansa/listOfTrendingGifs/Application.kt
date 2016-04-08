package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.Store
import com.brianegan.bansa.applyMiddleware
import com.brianegan.bansa.createStore
import com.brianegan.bansa.listOfTrendingGifs.actions.INIT
import com.brianegan.bansa.listOfTrendingGifs.middleware.GifMiddleware
import com.brianegan.bansa.listOfTrendingGifs.middleware.LoggingMiddleware
import com.brianegan.bansa.listOfTrendingGifs.reducers.applicationReducer
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.fullType
import uy.kohesive.injekt.api.get

class Application : android.app.Application() {
    companion object : InjektMain() {
        override fun InjektRegistrar.registerInjectables() {
            addSingleton(
                    fullType<Store<ApplicationState, Any>>(),
                    applyMiddleware(GifMiddleware, LoggingMiddleware)(createStore(ApplicationState(), applicationReducer)));
        }
    }

    val store = Injekt.get(fullType<Store<ApplicationState, Any>>())

    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT)
    }
}
