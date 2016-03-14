package com.brianegan.bansa

fun <S, A> applyMiddleware(
        vararg middleware: (Store<S, A>) -> ((A) -> A) -> (A) -> A)
        : (Store<S, A>) -> Store<S, A> {
    return { store ->
        store.dispatch = compose(middleware.map { it(store) })(store.dispatch)
        store
    }
}

fun <T> compose(
        chain: List<((T) -> T) -> (T) -> T>)
        : ((T) -> T) -> (T) -> T {
    return { dispatch -> chain.foldRight(dispatch, { f, composed -> f(composed) }) }
}
