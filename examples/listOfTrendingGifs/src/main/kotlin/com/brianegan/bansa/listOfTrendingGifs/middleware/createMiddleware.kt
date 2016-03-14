package com.brianegan.bansa.listOfTrendingGifs.middleware

import com.brianegan.bansa.Action
import com.brianegan.bansa.Store

fun <S, A : Action>createMiddleware(middleware: (Store<S, A>, (A) -> A, A) -> A)
        : (Store<S, A>) -> ((A) -> A) -> (A) -> A {
    return { store: Store<S, A> ->
        { next: (A) -> A ->
            { action: A ->
                middleware(store, next, action);
            }
        }
    }
}
