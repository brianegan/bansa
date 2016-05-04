package com.brianegan.bansa.listOfTrendingDGifs

import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.middleware.GifMiddleware
import com.brianegan.bansa.listOfTrendingGifs.middleware.LoggingMiddleware
import com.brianegan.bansa.listOfTrendingGifs.reducers.ApplicationReducer
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansaDevTools.DevToolsStore

val store: Store<ApplicationState> = DevToolsStore(ApplicationState(), ApplicationReducer(), GifMiddleware(), LoggingMiddleware())
