package com.brianegan.bansa.counter

import com.brianegan.bansa.createStore

val store = createStore(ApplicationState(), counterReducer)
