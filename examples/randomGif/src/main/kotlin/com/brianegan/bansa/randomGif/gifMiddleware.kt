package com.brianegan.bansa.randomGif

import com.brianegan.bansa.Store

val gifMiddleware = { store: Store<ApplicationState, Any> ->
    { next: (Any) -> Any ->
        { action: Any ->
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

            action
        }
    }
}
