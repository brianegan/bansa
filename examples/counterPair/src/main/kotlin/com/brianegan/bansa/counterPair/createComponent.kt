package com.brianegan.bansa.counterPair

import com.brianegan.bansa.Action
import com.brianegan.bansa.Store

fun <A : Action, S, VM> connect(
        mapStoreToViewModel: (Store<S, A>) -> VM)
        : ((VM) -> Unit) -> (Store<S, A>) -> Unit {

    return { view -> { store ->
            view(mapStoreToViewModel(store))
        }
    }


}
