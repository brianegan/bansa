package com.brianegan.bansa

fun <S, A> createTestStore(initialState: S, reducer: (S, A) -> S): Store<S, A> {
    return createStore(initialState, reducer)
}
