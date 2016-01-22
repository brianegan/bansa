package com.brianegan.bansa

import rx.Scheduler
import rx.schedulers.Schedulers

fun <S : State, A : Action> createStore(
        initialState: S,
        reducer: (S, A) -> S,
        scheduler: Scheduler = Schedulers.newThread())
        : Store<S, A>
        = BaseStore<S, A>(initialState, reducer, scheduler)
