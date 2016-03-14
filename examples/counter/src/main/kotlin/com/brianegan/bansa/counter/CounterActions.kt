package com.brianegan.bansa.counter

interface CounterAction {}

sealed class CounterActions {
    object INIT : CounterAction

    object INCREMENT : CounterAction

    object DECREMENT : CounterAction
}
