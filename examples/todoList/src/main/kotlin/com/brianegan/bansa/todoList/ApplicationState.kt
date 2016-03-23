package com.brianegan.bansa.todoList

import com.github.andrewoma.dexx.kollection.ImmutableList
import com.github.andrewoma.dexx.kollection.immutableListOf
import java.util.*

data class ApplicationState(val newTodoMessage: String = "", val todos: ImmutableList<Todo> = immutableListOf())

data class Todo(val message: String, val completed: Boolean = false, val id: UUID = UUID.randomUUID())
