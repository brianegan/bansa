package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store

val gifMiddleware = { store: Store<ApplicationState, Action> ->
    { next: (Action) -> Action ->
        { action: Action ->
            when (action) {
                is REFRESH -> {
                    store.getState().currentRequest.unsubscribe()

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
