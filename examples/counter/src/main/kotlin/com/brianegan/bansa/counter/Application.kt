package com.brianegan.bansa.counter

import com.brianegan.bansa.Store
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.get

class Application : android.app.Application() {
    val injektMain = object : InjektMain() {
        override fun InjektRegistrar.registerInjectables() {
            importModule(StoreModule())
        }
    }

    override fun onCreate() {
        super.onCreate()

        val store = Injekt.get<Store<ApplicationState, Any>>()
        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}
