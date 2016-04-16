package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Middleware

val gifMiddleware = Middleware<ApplicationState, Any> { store, action, next ->
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
}
