package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.Action

val loggingMiddleware = createMiddleware<ApplicationState, Action> { store, next, action ->
    println(action.toString());
    next(action)
}
