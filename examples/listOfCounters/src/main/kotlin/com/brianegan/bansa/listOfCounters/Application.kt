package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.Store
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
                    com.brianegan.bansa.createStore(ApplicationState(), applicationReducer));
        }
    }

    val store = Injekt.get(fullType<Store<ApplicationState, Any>>())

    override fun onCreate() {
        super.onCreate()
        store.dispatch(INIT())
    }
}
