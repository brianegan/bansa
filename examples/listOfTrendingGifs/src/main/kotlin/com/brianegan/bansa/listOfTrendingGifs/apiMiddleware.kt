package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.Action
import com.brianegan.bansa.listOfTrendingGifs.api.fetchTrendingGifs

val gifMiddleware = createMiddleware<ApplicationState, Action> { store, next, action ->
    val state = store.getState()

    when (action) {
        is FETCH_NEXT_PAGE -> {
            val subscription = fetchTrendingGifs(state.pagination.offset, state.pagination.count).subscribe({
                next(FETCH_COMPLETED(it))
            }, {
                throw it
            })

            next(FETCH_NEXT_PAGE_STARTED(subscription))
        }
        is REFRESH -> {
            state.currentRequest.unsubscribe()

            val subscription = fetchTrendingGifs().subscribe({
                next(FETCH_COMPLETED(it))
            }, {
                throw it
            })

            next(REFRESH_STARTED(subscription))
        }
        else -> next(action)
    }

    action
}
