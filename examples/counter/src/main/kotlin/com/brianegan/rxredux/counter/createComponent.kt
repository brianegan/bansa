package com.brianegan.rxredux.counter

import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.State
import com.brianegan.RxRedux.Store

fun <A : Action, S : State, VM> connect(
        mapStoreToViewModel: (Store<S, A>) -> VM)
        : ((VM) -> Unit) -> (Store<S, A>) -> Unit {

    return { view -> { store ->
            view(mapStoreToViewModel(store))
        }
    }


}
