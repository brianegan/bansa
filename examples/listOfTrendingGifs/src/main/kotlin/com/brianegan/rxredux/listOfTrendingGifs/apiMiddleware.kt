package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import com.brianegan.rxredux.listOfTrendingGifs.api.fetchTrendingGifs

val gifMiddleware = { store: Store<ApplicationState, Action> ->
    { next: (Action) -> Action ->
        { action: Action ->
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
    }
}
