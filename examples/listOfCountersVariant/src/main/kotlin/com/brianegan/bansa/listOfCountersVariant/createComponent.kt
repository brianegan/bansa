package com.brianegan.bansa.listOfCountersVariant

import com.brianegan.bansaKotlin.Store

fun <A, S, VM> connect(
        mapStoreToViewModel: (Store<S, A>) -> VM)
        : ((VM) -> Unit) -> (Store<S, A>) -> Unit {

    return { view -> { store ->
            view(mapStoreToViewModel(store))
        }
    }


}
