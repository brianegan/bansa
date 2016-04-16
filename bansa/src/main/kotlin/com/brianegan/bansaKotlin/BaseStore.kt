package com.brianegan.bansaKotlin

class BaseStore<S, A>(override var state: S, val reducer: (S, A) -> S) : com.brianegan.bansaKotlin.Store<S, A> {
    val onStateChangeCallbacks = mutableListOf<(S) -> Unit>()

    override var dispatch: (action: A) -> Unit = { action ->
        state = reducer(state, action);
        onStateChangeCallbacks.forEach { it(state) }
    }

    override fun subscribe(onStateChange: (S) -> Unit): com.brianegan.bansaKotlin.Subscription {
        onStateChangeCallbacks.add(onStateChange)

        return object : com.brianegan.bansaKotlin.Subscription {
            override fun unsubscribe() {
                onStateChangeCallbacks.remove(onStateChange)
            }
        }
    }
}
