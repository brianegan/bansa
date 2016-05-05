package com.brianegan.bansa.todoList

import com.brianegan.bansa.Reducer
import com.github.andrewoma.dexx.kollection.toImmutableList

val applicationReducer = Reducer <ApplicationState>{ state, action ->
    when (action) {
        is INIT -> ApplicationState()
        is ADD ->
            if (state.newTodoMessage.length > 0) {
                state.copy(
                        newTodoMessage = "",
                        todos = state.todos.plus(Todo(action.message)))
            } else {
                state
            }
        is REMOVE -> state.copy(
                todos = state.todos.filter({ it.id.equals(action.id) }).toImmutableList())
        is TOGGLE_TODO -> state.copy(
                todos = state.todos.map({
                    if (it.id.equals(action.id)) {
                        it.copy(completed = action.completed)
                    } else {
                        it
                    }
                }).toImmutableList())
        is UPDATE_NEW_TODO_MESSAGE -> state.copy(
                newTodoMessage = action.message)
        else -> state
    }
}
