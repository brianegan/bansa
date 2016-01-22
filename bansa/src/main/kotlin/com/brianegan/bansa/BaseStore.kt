package com.brianegan.bansa

import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.Subscription
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

public class BaseStore<S : State, A : Action>(
        private val initialState: S,
        private val initialReducer: (S, A) -> S,
        private val scheduler: Scheduler = Schedulers.newThread()
) : Store<S, A>(initialState, initialReducer, scheduler) {
    private val dispatcher: SerializedSubject<A, A>
    override val state: Observable<S>
    private var currentState: S
    public var reducer: (S, A) -> S

    init {
        reducer = initialReducer
        currentState = initialState

        dispatcher = SerializedSubject<A, A>(PublishSubject.create<A>())
        state = dispatcher // When an action is dispatched
                .observeOn(scheduler) // Run the scan on a given thread, by default a background thread
                .scan(currentState, { state, action -> reducer(state, action) }) // Run the action through your reducers, producing a new state
                .doOnNext({ newState -> currentState = newState }) // Update the state field of the instance for lazy access
                .publish() // Turn it into a ConnectableObservable so all subscribers receive the same values
                .refCount()

        state.subscribe()
    }

    override fun getState(): S = currentState

    override var dispatch: (action: A) -> A = { action ->
        dispatcher.onNext(action)
        action
    }

    override fun subscribe(subscriber: Subscriber<S>): Subscription {
        return state.subscribe(subscriber)
    }
}
