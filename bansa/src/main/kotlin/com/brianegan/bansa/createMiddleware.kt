package com.brianegan.bansa

fun <S, A> createMiddleware(middleware: (Store<S, A>, (A) -> Unit, A) -> Unit)
        : (Store<S, A>) -> ((A) -> Unit) -> (A) -> Unit {
    return { store: Store<S, A> ->
        { next: (A) -> Unit ->
            { action: A ->
                middleware(store, next, action);
            }
        }
    }
}
