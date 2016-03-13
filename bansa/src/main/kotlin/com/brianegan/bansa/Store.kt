package com.brianegan.bansa

import rx.Observable
import rx.Subscriber
import rx.Subscription

interface Store<S : State, A : Action> {
    val state: Observable<S>
    fun getState(): S
    var dispatch: (action: A) -> A
    fun subscribe(subscriber: Subscriber<S>): Subscription
}
