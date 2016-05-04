package com.brianegan.bansa.todoList

import com.brianegan.bansa.Action
import java.util.*

data class INIT(val state : ApplicationState = ApplicationState()) : Action
data class ADD(val message: String) : Action
data class REMOVE(val id : UUID) : Action
data class TOGGLE_TODO(val id : UUID, val completed: Boolean) : Action
data class UPDATE_NEW_TODO_MESSAGE(val message: String) : Action
