package com.brianegan.bansa.counter

import com.brianegan.bansa.Action
import com.brianegan.bansa.Store
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
                    fullType<Store<ApplicationState, Action>>(),
                    createStore(ApplicationState(), applicationReducer));
        }
    }

    val store = Injekt.get(fullType<Store<ApplicationState, Action>>())

    override fun onCreate() {
        super.onCreate()
        store.dispatch(CounterActions.INIT) // Initialize the store
    }
}
