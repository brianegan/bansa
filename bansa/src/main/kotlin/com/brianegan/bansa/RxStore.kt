package com.brianegan.bansa

import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

class RxStore<S, A>(
        private val initialState: S,
        private val initialReducer: (S, A) -> S,
        private val scheduler: Scheduler = Schedulers.newThread()
) : Store<S, A> {
    private val dispatcher: SerializedSubject<A, A>
    override var state: S = initialState

    override val stateChanges: Observable<S>
    var reducer: (S, A) -> S

    init {
        reducer = initialReducer
        dispatcher = SerializedSubject<A, A>(PublishSubject.create<A>())
        stateChanges = dispatcher // When an action is dispatched
                .observeOn(scheduler) // Run the scan on a given thread, by default a background thread
                .scan(state, { state, action -> reducer(state, action) }) // Run the action through your reducers, producing a new state
                .doOnNext({ newState -> state = newState }) // Update the state field of the instance for lazy access
                .share() // Share the Observable so all subscribers receive the same values

        stateChanges.subscribe()
    }

    override var dispatch: (action: A) -> A = { action ->
        dispatcher.onNext(action)
        action
    }

    override fun subscribe(subscriber: Subscriber<S>): Subscription {
        return stateChanges.subscribe(subscriber)
    }
}
