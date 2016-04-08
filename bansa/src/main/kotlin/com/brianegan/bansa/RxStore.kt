package com.brianegan.bansa

import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

class RxStore<S, A>(
        override var state: S,
        val reducer: (S, A) -> S
) : Store<S, A> {

    private val dispatcher = SerializedSubject<A, A>(PublishSubject.create<A>())

    override val stateChanges: Observable<S>

    init {
        stateChanges = dispatcher // When an action is dispatched
                .scan(state, { state, action -> reducer(state, action) }) // Run the action through your reducers, producing a new state
                .doOnNext { newState -> state = newState } // Update the state field of the instance for lazy access
                .share() // Share the Observable so all subscribers receive the same values

        stateChanges.subscribe()
    }

    override fun dispatch(action: A): A {
        dispatcher.onNext(action)
        return action
    }

    override fun subscribe(subscriber: Subscriber<S>): Subscription {
        return stateChanges.subscribe(subscriber)
    }
}
