package com.brianegan.bansa.todoList

import com.brianegan.bansa.Store

fun <A, S, VM> connect(
        mapStoreToViewModel: (Store<S, A>) -> VM)
        : ((VM) -> Unit) -> (Store<S, A>) -> Unit {

    return { view -> { store ->
            view(mapStoreToViewModel(store))
        }
    }


}
