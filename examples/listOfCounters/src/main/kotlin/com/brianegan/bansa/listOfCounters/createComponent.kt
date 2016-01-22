package com.brianegan.bansa.listOfCounters

import com.brianegan.bansa.Action
import com.brianegan.bansa.State
import com.brianegan.bansa.Store

fun <A : Action, S : State, VM> connect(
        mapStoreToViewModel: (Store<S, A>) -> VM)
        : ((VM) -> Unit) -> (Store<S, A>) -> Unit {

    return { view -> { store ->
            view(mapStoreToViewModel(store))
        }
    }


}
