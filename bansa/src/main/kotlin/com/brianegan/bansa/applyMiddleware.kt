package com.brianegan.bansa

fun <S, A> applyMiddleware(
        vararg middleware: (Store<S, A>) -> ((A) -> Unit) -> (A) -> Unit)
        : (Store<S, A>) -> Store<S, A> {
    return { store ->
        store.dispatch = compose(middleware.map { it(store) })(store.dispatch)
        store
    }
}

fun <T> compose(functions: List<((T) -> Unit) -> (T) -> Unit>): ((T) -> Unit) -> (T) -> Unit {
    return { x -> functions.foldRight(x, { f, composed -> f(composed) }) }
}
