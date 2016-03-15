package com.brianegan.bansa

import rx.Observable
import rx.Subscriber
import rx.Subscription

interface Store<S, A> {
    val stateChanges: Observable<S>
    val state: S
    val dispatch: (action: A) -> A
    fun subscribe(subscriber: Subscriber<S>): Subscription
}
