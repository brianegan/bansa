package com.brianegan.betterreads

import com.brianegan.RxRedux.Action

interface CounterAction : Action {}

sealed class CounterActions {
    object INIT : CounterAction

    object INCREMENT : CounterAction

    object DECREMENT : CounterAction
}
