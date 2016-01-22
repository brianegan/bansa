package com.brianegan.bansa

import java.util.*

fun <A : Action, S : State> combineReducers(vararg reducers: (S, A) -> S): (S, A) -> S =
        { state: S, action: A ->
            Arrays.asList(*reducers).fold(state, { state, reducer -> reducer(state, action) })
        }
