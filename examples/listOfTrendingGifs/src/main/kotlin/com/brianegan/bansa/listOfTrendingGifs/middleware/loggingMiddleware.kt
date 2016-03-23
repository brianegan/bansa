package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.Middleware
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

object LoggingMiddleware : Middleware<ApplicationState, Any> {
    override fun intercept(store: Store<ApplicationState, Any>, next: (Any) -> Any, action: Any): Any {
        println("Before ${action.toString()}: ${store.state}")
        next(action)
        println("After ${action.toString()}:  ${store.state}")
        return action
    }
}
