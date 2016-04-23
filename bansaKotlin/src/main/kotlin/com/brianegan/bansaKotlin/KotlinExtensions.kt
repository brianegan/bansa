package com.brianegan.bansaKotlin

import com.brianegan.bansa.Middleware
import com.brianegan.bansa.NextDispatcher
import com.brianegan.bansa.Reducer
import com.brianegan.bansa.Store

operator fun <S, A> Reducer<S, A>.invoke(state: S, action: A): S {
    return reduce(state, action)
}

operator fun <S, A> Middleware<S, A>.invoke(store: Store<S, A>, action: A, next: NextDispatcher<A>) {
    dispatch(store, action, next);
}

operator fun <A> NextDispatcher<A>.invoke(action: A) {
    dispatch(action)
}
