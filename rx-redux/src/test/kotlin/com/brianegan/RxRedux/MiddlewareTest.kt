package com.brianegan.RxRedux

import rx.Observable
import rx.schedulers.TestScheduler
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import org.junit.Test as test

class MiddlewareTest {
    data class MyState(val state: String = "initial state") : State
    data class MyAction(val type: String = "unknown") : Action

    @test fun middlewareShouldBeCalled() {
        var counter = 0
        val middleWare = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    next(action)
                    action
                }
            }
        }

        val reducer = { state: MyState, action: MyAction ->
            state
        }

        val store = applyMiddleware<MyState, MyAction>(middleWare)(createTestStore(MyState(), reducer))
        store.dispatch(MyAction(type = "hey hey!"))
        assertEquals(MyState(), store.getState())
        assertEquals(counter, 1)
    }

    @test fun middlewareShouldPassRecursivelyThroughTheChain() {
        var counter = 0
        var order = ArrayList<String>()

        val middleWare1 = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    order.add("first")
                    val nextAction = next(action)
                    order.add("third")
                    nextAction
                }
            }
        }

        val middleWare2 = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    order.add("second")
                    val nextAction = next(action)
                    nextAction
                }
            }
        }

        val reducer = { state: MyState, action: MyAction ->
            when (action.type) {
                "hey hey!"  -> MyState(state="howdy!")
                else -> state
            }
        }

        val store = applyMiddleware<MyState, MyAction>(middleWare1, middleWare2)(createTestStore(MyState(), reducer))
        store.dispatch(MyAction(type = "hey hey!"))
        assertEquals(MyState("howdy!"), store.getState())
        assertEquals(2, counter)
        assertEquals(arrayListOf("first", "second", "third"), order)
    }

    @test fun middlewareShouldWorkAsync() {
        var counter = 0
        var order = ArrayList<String>()
        val testScheduler = TestScheduler()

        val fetchMiddleware = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    when (action.type) {
                        "CALL_API" -> {
                            order.add("FETCHING")
                            next(MyAction("FETCHING"))
                            Observable
                                    .just(5)
                                    .delay(1L, TimeUnit.SECONDS, testScheduler)
                                    .subscribe({
                                        order.add("FETCH_COMPLETE")
                                        next(MyAction("FETCH_COMPLETE"))
                                    })

                            next(action)
                        }
                        else -> next(action)
                    }
                }
            }
        }

        val loggerMiddleware = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    order.add("LOGGING THE ACTION")
                    next(action)
                }
            }
        }

        val reducer = { state: MyState, action: MyAction ->
            when (action.type) {
                "FETCHING"  -> MyState(state="FETCHING")
                "FETCH_COMPLETE"  -> MyState(state="FETCH_COMPLETE")
                else -> state
            }
        }

        val store = applyMiddleware<MyState, MyAction>(fetchMiddleware, loggerMiddleware)(createTestStore(MyState(), reducer))
        store.dispatch(MyAction(type = "CALL_API"))
        assertEquals(3, counter)
        assertEquals(arrayListOf("FETCHING", "LOGGING THE ACTION", "LOGGING THE ACTION"), order)
        assertEquals(MyState("FETCHING"), store.getState())
        testScheduler.advanceTimeBy(2L, TimeUnit.SECONDS)
        assertEquals(4, counter)
        assertEquals(arrayListOf("FETCHING", "LOGGING THE ACTION", "LOGGING THE ACTION", "FETCH_COMPLETE", "LOGGING THE ACTION"), order)
        assertEquals(MyState(state="FETCH_COMPLETE"), store.getState())
    }

    @test fun middlewareCanPassThroughTheWholeChainAgain() {
        var counter = 0
        var order = ArrayList<String>()
        val testScheduler = TestScheduler()

        val middleWare1 = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    order.add("first")

                    // Redispatch an action that goes through the whole chain
                    // (useful for async middleware)
                    val nextAction = next(action)

                    if (action.type == "around!") {
                        store.dispatch(MyAction());
                    }

                    nextAction
                }
            }
        }

        val middleWare2 = { store: Store<MyState, MyAction> ->
            { next: (MyAction) -> MyAction ->
                { action: MyAction ->
                    counter += 1
                    order.add("second")
                    next(action)
                }
            }
        }

        val reducer = { state: MyState, action: MyAction ->
            state
        }

        val store = applyMiddleware<MyState, MyAction>(middleWare1, middleWare2)(createTestStore(MyState(), reducer))
        store.dispatch(MyAction(type = "around!"))
        assertEquals(4, counter)
        assertEquals(arrayListOf("first", "second", "first", "second"), order)
    }

}
