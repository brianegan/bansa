package com.brianegan.bansa.todoList

import java.util.*

data class INIT(val state : ApplicationState = ApplicationState()) : AppAction
data class ADD(val message: String) : AppAction
data class REMOVE(val id : UUID) : AppAction
data class TOGGLE_TODO(val id : UUID, val completed: Boolean) : AppAction
data class UPDATE_NEW_TODO_MESSAGE(val message: String) : AppAction
