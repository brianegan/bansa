package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.listOfTrendingGifs.actions.*
import com.brianegan.bansa.listOfTrendingGifs.api.fetchTrendingGifs
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

val gifMiddleware = createMiddleware<ApplicationState, Any> { store, next, action ->
    val state = store.state

    when (action) {
        is FETCH_NEXT_PAGE -> {
            val subscription = fetchTrendingGifs(state.pagination.offset, state.pagination.count).subscribe({
                next(FETCH_NEXT_PAGE_COMPLETED(it))
            }, {
                throw it
            })

            next(FETCH_NEXT_PAGE_STARTED(subscription))
        }
        is REFRESH -> {
            state.currentRequest.unsubscribe()

            val subscription = fetchTrendingGifs().subscribe({
                next(REFRESH_COMPLETED(it))
            }, {
                throw it
            })

            next(REFRESH_STARTED(subscription))
        }
        else -> next(action)
    }

    action
}
