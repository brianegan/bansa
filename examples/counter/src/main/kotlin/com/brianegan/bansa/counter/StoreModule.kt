package com.brianegan.bansa.counter

import com.brianegan.bansa.Store
import com.brianegan.bansa.createStore
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton

class StoreModule : InjektModule {
    override fun InjektRegistrar.registerInjectables() {
        addSingleton<Store<ApplicationState, Any>>(createStore(ApplicationState(), counterReducer))
    }
}
