package com.brianegan.bansa

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class MiddlewareSpec : Spek() {
    data class MyState(val state: String = "initial state") : State
    data class MyAction(val type: String = "unknown") : Action

    init {
        given("a store with middleware") {
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

            on("action fired") {
                store.dispatch(MyAction(type = "hey hey!"))

                it("should run the action through the middleware") {
                    assertThat(store.getState()).isEqualTo(MyState())
                    assertThat(counter).isEqualTo(1)
                }
            }
        }

        given("a chain of middleware") {
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
                    "hey hey!" -> MyState(state = "howdy!")
                    else -> state
                }
            }

            val store = applyMiddleware<MyState, MyAction>(middleWare1, middleWare2)(createTestStore(MyState(), reducer))

            on("action fired") {
                store.dispatch(MyAction(type = "hey hey!"))

                it("should pass through the middleware chain") {
                    assertThat(store.getState()).isEqualTo(MyState("howdy!"))
                    assertThat(counter).isEqualTo(2)
                    assertThat(order).isEqualTo(arrayListOf("first", "second", "third"))
                }
            }
        }

        given("async middleware") {
            var counter = 0
            var order = ArrayList<String>()
            val testScheduler = rx.schedulers.TestScheduler()

            val fetchMiddleware = { store: Store<MyState, MyAction> ->
                { next: (MyAction) -> MyAction ->
                    { action: MyAction ->
                        counter += 1
                        when (action.type) {
                            "CALL_API" -> {
                                next(MyAction("FETCHING"))
                                Observable
                                        .just(5)
                                        .delay(1L, TimeUnit.SECONDS, testScheduler)
                                        .subscribe({
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
                        order.add(action.type)
                        next(action)
                    }
                }
            }

            val reducer = { state: MyState, action: MyAction ->
                when (action.type) {
                    "FETCHING" -> MyState(state = "FETCHING")
                    "FETCH_COMPLETE" -> MyState(state = "FETCH_COMPLETE")
                    else -> state
                }
            }

            val store = applyMiddleware<MyState, MyAction>(fetchMiddleware, loggerMiddleware)(createTestStore(MyState(), reducer))

            on("async action fired") {
                store.dispatch(MyAction(type = "CALL_API"))

                it("should be able to dispatch the first few events immediately") {
                    assertThat(counter).isEqualTo(3)
                    assertThat(order).isEqualTo(arrayListOf("FETCHING", "CALL_API"))
                    assertThat(store.getState()).isEqualTo(MyState("FETCHING"))
                }

                it("should be able to dispatch events later on after async operations") {
                    testScheduler.advanceTimeBy(2L, TimeUnit.SECONDS)
                    assertThat(counter).isEqualTo(4)
                    assertThat(order).isEqualTo(arrayListOf("FETCHING", "CALL_API", "FETCH_COMPLETE"))
                    assertThat(store.getState()).isEqualTo(MyState(state = "FETCH_COMPLETE"))
                }
            }
        }

        given("a chain of middleware that re-dispatch certain actions") {
            var counter = 0
            val order = ArrayList<String>()

            val middleWare1 = { store: Store<MyState, MyAction> ->
                { next: (MyAction) -> MyAction ->
                    { action: MyAction ->
                        counter += 1
                        order.add("first")

                        val nextAction = next(action)

                        // Redispatch an action that goes through the whole chain
                        // (useful for async middleware)
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

            on("action fired") {
                store.dispatch(MyAction(type = "around!"))

                it("should dispatch a second action that passes through both middleware") {
                    assertThat(counter).isEqualTo(4)
                    assertThat(order).isEqualTo(arrayListOf("first", "second", "first", "second"))
                }
            }
        }
    }
}
