package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.Middleware
import com.brianegan.bansa.NextDispatcher
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansaKotlin.invoke

class LoggingMiddleware : Middleware<ApplicationState, Any> {
    override fun dispatch(store: Store<ApplicationState, Any>, action: Any, next: NextDispatcher<Any>) {
        println("Before ${action.javaClass.canonicalName}: ${store.state}")
        next(action)
        println("After ${action.javaClass.canonicalName}:  ${store.state}")
    }
}
