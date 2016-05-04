package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.Action
import com.brianegan.bansa.Middleware
import com.brianegan.bansa.NextDispatcher
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.actions.*
import com.brianegan.bansa.listOfTrendingGifs.api.fetchTrendingGifs
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansaKotlin.invoke
import rx.subscriptions.Subscriptions

class GifMiddleware : Middleware<ApplicationState> {
    private var currentRequest = Subscriptions.empty()

    override fun dispatch(store: Store<ApplicationState>, action: Action, next: NextDispatcher) {
        when (action) {
            is FETCH_NEXT_PAGE -> {
                currentRequest = fetchTrendingGifs(
                        store.state.pagination.offset,
                        store.state.pagination.count).subscribe({
                    next(FETCH_NEXT_PAGE_COMPLETED(it))
                }, {
                    throw it
                })

                next(FETCH_NEXT_PAGE_STARTED)
            }
            is REFRESH -> {
                currentRequest.unsubscribe()

                currentRequest = fetchTrendingGifs().subscribe({
                    next(REFRESH_COMPLETED(it))
                }, {
                    throw it
                })

                next(REFRESH_STARTED)
            }
            else -> next(action)
        }
    }
}
