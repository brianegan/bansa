package com.brianegan.bansaKotlin

interface Store<S, A> {
    val state: S
    var dispatch: (action: A) -> Unit
    fun subscribe(onStateChange: (S) -> Unit): com.brianegan.bansaKotlin.Subscription
}
