package com.brianegan.bansa.randomGif

import com.brianegan.bansa.createMiddleware

val gifMiddleware = createMiddleware<ApplicationState, Any> { store, next, action ->
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
