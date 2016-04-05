package com.brianegan.bansa

class BaseStore<S, A>(override var state: S, val reducer: (S, A) -> S) : Store<S, A> {
    val onStateChangeCallbacks = mutableListOf<(S) -> Unit>()

    override var dispatch: (action: A) -> Unit = { action ->
        state = reducer(state, action);
        onStateChangeCallbacks.forEach { it(state) }
    }

    override fun subscribe(onStateChange: (S) -> Unit): Subscription {
        onStateChangeCallbacks.add(onStateChange)

        return Subscription {
            onStateChangeCallbacks.remove(onStateChange)
        }
    }
}
