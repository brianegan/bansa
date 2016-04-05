package com.brianegan.bansa

fun <S, A> createStore(initialState: S, reducer: (S, A) -> S): Store<S, A> = BaseStore(initialState, reducer)
