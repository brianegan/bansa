package com.brianegan.bansa

fun <S, A> createStore(initialState: S, reducer: (S, A) -> S) = RxStore(initialState, reducer)