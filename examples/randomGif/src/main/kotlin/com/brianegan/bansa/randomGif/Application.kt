package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Store
import com.brianegan.bansa.applyMiddleware
import com.brianegan.bansa.createStore
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
                    (applyMiddleware(gifMiddleware)(createStore(ApplicationState(), applicationReducer))))
        }
    }

    val store = Injekt.get<Store<ApplicationState, Any>>()

    override fun onCreate() {
        super.onCreate()

        // Initialize the store and immediately fetch a random gif!
        store.dispatch(INIT)
        store.dispatch(FETCH_RANDOM_GIF)
    }
}
