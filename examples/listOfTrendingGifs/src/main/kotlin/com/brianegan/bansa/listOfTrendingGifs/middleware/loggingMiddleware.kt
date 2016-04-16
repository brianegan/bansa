package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.createMiddleware
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

val loggingMiddleware = createMiddleware<ApplicationState, Any> { store, next, action ->
    println("Before ${action.javaClass.canonicalName}: ${store.state}")
    next(action)
    println("After ${action.javaClass.canonicalName}:  ${store.state}")
}
