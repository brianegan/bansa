package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

val loggingMiddleware = createMiddleware<ApplicationState, Any> { store, next, action ->
    println("Before ${action.toString()}: ${store.state}")
    next(action)
    println("After ${action.toString()}:  ${store.state}")
    action
}
