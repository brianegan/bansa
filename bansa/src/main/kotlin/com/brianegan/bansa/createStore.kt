package com.brianegan.bansa

import rx.Scheduler
import rx.schedulers.Schedulers

fun <S, A> createStore(
        initialState: S,
        reducer: (S, A) -> S,
        scheduler: Scheduler = Schedulers.newThread())
        : Store<S, A>
        = RxStore<S, A>(initialState, reducer, scheduler)
