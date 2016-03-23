package com.brianegan.bansa.counter

import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar

class Application : android.app.Application() {
    val injektMain = object : InjektMain() {
        override fun InjektRegistrar.registerInjectables() {
            importModule(StoreModule())
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
