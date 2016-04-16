package com.brianegan.bansa.todoList

import android.widget.LinearLayout
import com.brianegan.bansaKotlin.Store
import trikita.anvil.DSL.*

fun todoView(model: TodoViewModel) {
    linearLayout {
        size(FILL, WRAP)
        orientation(LinearLayout.HORIZONTAL)
        margin(0, dip(10))
        onClick {
            model.clickHandler()
        }

        textView {
            size(0, WRAP)
            weight(1f)
            text(model.todo.message)
            padding(0, dip(4))
            textSize(sip(16F))
        }

        checkBox {
            size(WRAP, WRAP)
            checked(model.todo.completed)
        }
    }
}

fun buildMapCounterToCounterViewModel(store: Store<ApplicationState, Any>): (Todo) -> TodoViewModel {
    return { todo ->
        val (id) = todo

        TodoViewModel(
                todo,
                { store.dispatch(TOGGLE_TODO(todo.id, !todo.completed)) }
        )
    }
}

data class TodoViewModel(val todo: Todo, val clickHandler: () -> Unit)
