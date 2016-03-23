package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Middleware
import com.brianegan.bansa.Store

object GifMiddleware : Middleware<ApplicationState, Any> {

    override fun intercept(store: Store<ApplicationState, Any>, next: (Any) -> Any, action: Any): Any {
        when (action) {
            is FETCH_RANDOM_GIF -> {
                next(FETCHING)

                randomGif().subscribe({
                    next(NEW_RANDOM_GIF(it))
                }, {
                    throw it
                })
            }
            else -> next(action)
        }

        return action
    }
}
