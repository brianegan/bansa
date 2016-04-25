package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.Middleware
import com.brianegan.bansa.NextDispatcher
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.actions.*
import com.brianegan.bansa.listOfTrendingGifs.api.fetchTrendingGifs
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansaKotlin.invoke

class GifMiddleware : Middleware<ApplicationState, Any> {
    override fun dispatch(store: Store<ApplicationState, Any>, action: Any, next: NextDispatcher<Any>) {
        when (action) {
            is FETCH_NEXT_PAGE -> {
                val subscription = fetchTrendingGifs(
                        store.state.pagination.offset,
                        store.state.pagination.count).subscribe({
                    next(FETCH_NEXT_PAGE_COMPLETED(it))
                }, {
                    throw it
                })

                next(FETCH_NEXT_PAGE_STARTED(subscription))
            }
            is REFRESH -> {
                store.state.currentRequest.unsubscribe()

                val subscription = fetchTrendingGifs().subscribe({
                    next(REFRESH_COMPLETED(it))
                }, {
                    throw it
                })

                next(REFRESH_STARTED(subscription))
            }
            else -> next(action)
        }
    }
}
