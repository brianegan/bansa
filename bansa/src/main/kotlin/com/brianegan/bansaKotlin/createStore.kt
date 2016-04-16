package com.brianegan.bansaKotlin

fun <S, A> createStore(initialState: S, reducer: (S, A) -> S): Store<S, A> = BaseStore(initialState, reducer)
