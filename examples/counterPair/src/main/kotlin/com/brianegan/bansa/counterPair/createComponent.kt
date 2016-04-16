package com.brianegan.bansa.counterPair

import com.brianegan.bansaKotlin.Store

fun <A : Any, S, VM> connect(
        mapStoreToViewModel: (Store<S, A>) -> VM)
        : ((VM) -> Unit) -> (Store<S, A>) -> Unit {

    return { view -> { store ->
            view(mapStoreToViewModel(store))
        }
    }


}
