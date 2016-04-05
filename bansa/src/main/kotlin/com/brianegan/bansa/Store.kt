package com.brianegan.bansa

interface Store<S, A> {
    val state: S
    var dispatch: (action: A) -> Unit
    fun subscribe(onStateChange: (S) -> Unit): Subscription
}
