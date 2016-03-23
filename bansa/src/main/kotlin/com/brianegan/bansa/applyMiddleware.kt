package com.brianegan.bansa

import rx.Observable
import rx.Subscriber
import rx.Subscription

/**
 * Store API subset exposed to middlewares.
 * The dispatch methods runs through all middlewares.
 */
private class MiddlewareStoreApi<S, A>(val store: Store<S, A>, val middlewares: Array<out (Store<S, A>) -> ((A) -> A) -> (A) -> A>): Store<S, A> {
    override val state: S
        get() = store.state

    override fun dispatch(action: A): A {
        return compose(middlewares.map { it(this) })({ store.dispatch(it) })(action)
    }

    override val stateChanges: Observable<S>
        get() = throw UnsupportedOperationException("stateChanges is not exposed to middlewares")

    override fun subscribe(subscriber: Subscriber<S>): Subscription {
        throw UnsupportedOperationException("subscribe is not exposed to middlewares")
    }
}

fun <S, A> applyMiddleware(
        vararg middlewares: (Store<S, A>) -> ((A) -> A) -> (A) -> A)
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

fun <T> compose(functions: List<(T) -> T>): (T) -> T {
    return { x -> functions.foldRight(x, { f, composed -> f(composed) }) }
}
