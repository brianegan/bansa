package com.brianegan.bansa.counter

import com.brianegan.bansa.Action

interface CounterAction : Action {}

sealed class CounterActions {
    object INIT : CounterAction

    object INCREMENT : CounterAction

    object DECREMENT : CounterAction
}
