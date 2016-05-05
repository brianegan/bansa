package com.brianegan.bansa.listOfTrendingDGifs

import com.brianegan.bansa.BaseStore
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.middleware.GifMiddleware
import com.brianegan.bansa.listOfTrendingGifs.middleware.LoggingMiddleware
import com.brianegan.bansa.listOfTrendingGifs.reducers.ApplicationReducer
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState

val store: Store<ApplicationState> = BaseStore(ApplicationState(), ApplicationReducer(), GifMiddleware(), LoggingMiddleware())
