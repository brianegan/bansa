package com.brianegan.RxRedux

import rx.schedulers.Schedulers

fun <S : State, A : Action> createTestStore(initialState: S, reducer: (S, A) -> S): Store<S, A> {
    return createStore(initialState, reducer, Schedulers.immediate());
}
