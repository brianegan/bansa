package com.brianegan.bansaKotlin

fun <S, A> applyMiddleware(
        vararg middleware: (com.brianegan.bansaKotlin.Store<S, A>) -> ((A) -> Unit) -> (A) -> Unit)
        : (com.brianegan.bansaKotlin.Store<S, A>) -> com.brianegan.bansaKotlin.Store<S, A> {
    return { store ->
        store.dispatch = compose(middleware.map { it(store) })(store.dispatch)
        store
    }
}

fun <T> compose(functions: List<((T) -> Unit) -> (T) -> Unit>): ((T) -> Unit) -> (T) -> Unit {
    return { x -> functions.foldRight(x, { f, composed -> f(composed) }) }
}
