package com.brianegan.bansa

import rx.Observable
import rx.Subscriber
import rx.Subscription

interface Middleware<S, A> {
    fun intercept(store: Store<S, A>, next: (A) -> A, action: A): A
}

/**
 * Store API subset exposed to middlewares.
 * The dispatch methods runs through all middlewares.
 */
private class MiddlewareStoreApi<S, A>(val store: Store<S, A>, val middlewares: Array<out Middleware<S, A>>): Store<S, A> {
    override val state: S
        get() = store.state

    override fun dispatch(action: A): A {
        var dispatch = { action: A -> store.dispatch(action) }
        middlewares.reversed().forEach { middleware ->
            val next = dispatch
            dispatch = { action -> middleware.intercept(this, next, action) }
        }

        return dispatch(action)
    }

    override val stateChanges: Observable<S>
        get() = throw UnsupportedOperationException("stateChanges is not exposed to middlewares")

    override fun subscribe(subscriber: Subscriber<S>): Subscription {
        throw UnsupportedOperationException("subscribe is not exposed to middlewares")
    }
}

fun <S, A> applyMiddleware(vararg middlewares: Middleware<S, A>)
        : (Store<S, A>) -> Store<S, A> {
    return { store ->
        val middlewareApi = MiddlewareStoreApi(store, middlewares)

        object : Store<S, A> by store {
            override fun dispatch(action: A): A {
                return middlewareApi.dispatch(action)
            }
        }
    }
}