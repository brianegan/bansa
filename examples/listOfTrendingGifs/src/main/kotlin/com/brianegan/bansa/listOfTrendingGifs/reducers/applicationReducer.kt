package com.brianegan.bansa.listOfTrendingGifs.reducers

import com.brianegan.bansa.Reducer
import com.brianegan.bansa.listOfTrendingGifs.actions.*
import com.brianegan.bansa.listOfTrendingGifs.api.NextPage
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import rx.subscriptions.Subscriptions

class ApplicationReducer : Reducer<ApplicationState, Any> {
    override fun invoke(state: ApplicationState, action: Any): ApplicationState {
        when (action) {
            is INIT -> return ApplicationState()

            is REFRESH_STARTED -> return state.copy(
                    isRefreshing = true,
                    currentRequest = action.subscription)

            is REFRESH_COMPLETED -> return state.copy(
                    isRefreshing = false,
                    gifs = action.payload.gifs,
                    pagination = NextPage(
                            offset = action.payload.pagination.offset.plus(action.payload.pagination.count)),
                    currentRequest = Subscriptions.empty())

            is FETCH_NEXT_PAGE_STARTED -> return state.copy(isFetching = true)

            is FETCH_NEXT_PAGE_COMPLETED -> return state.copy(
                    isFetching = false,
                    gifs = state.gifs.plus(action.payload.gifs),
                    pagination = NextPage(
                            offset = action.payload.pagination.offset.plus(action.payload.pagination.count)),
                    currentRequest = Subscriptions.empty())

            is ORIENTATION_CHANGE -> return state.copy(
                    orientation = action.orientation
            )

            else -> return state
        }
    }
}
