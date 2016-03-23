package com.brianegan.bansa.counter

import com.brianegan.bansa.Store
import com.brianegan.bansa.createStore
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.get

class Application : android.app.Application() {
    val injektMain = object : InjektMain() {
        override fun InjektRegistrar.registerInjectables() {
            addSingleton<Store<ApplicationState, Any>>(createStore(ApplicationState(), counterReducer))
        }
    }

    override fun onCreate() {
        super.onCreate()

        val store = Injekt.get<Store<ApplicationState, Any>>()
        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}
