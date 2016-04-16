package com.brianegan.bansa.todoList

import com.brianegan.bansa.createStore

val store = createStore(ApplicationState(), applicationReducer)
