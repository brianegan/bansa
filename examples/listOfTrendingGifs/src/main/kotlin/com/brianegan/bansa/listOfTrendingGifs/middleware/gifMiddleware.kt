package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.Middleware
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.actions.*
import com.brianegan.bansa.listOfTrendingGifs.api.fetchTrendingGifs
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

object GifMiddleware : Middleware<ApplicationState, Any> {
    override fun intercept(store: Store<ApplicationState, Any>, next: (Any) -> Any, action: Any): Any {
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

        return action
    }
}
