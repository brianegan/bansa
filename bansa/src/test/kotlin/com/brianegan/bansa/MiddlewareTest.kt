package com.brianegan.bansa

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class MiddlewareTest {
    data class MyState(val state: String = "initial state")
    data class MyAction(val type: String = "unknown")

    @Test
    fun `actions should be run through a store's middleware`() {
        var counter = 0
        val middleWare = createMiddleware<MyState, MyAction> { store, next, action ->
            counter += 1
            next(action)
        }

        val reducer = { state: MyState, action: MyAction ->
            state
        }

        val store = applyMiddleware(middleWare)(createStore(MyState(), reducer))

        store.dispatch(MyAction(type = "hey hey!"))

        assertThat(store.state).isEqualTo(MyState())
        assertThat(counter).isEqualTo(1)
    }

    @Test
    fun `actions should pass through the middleware chain in the correct order`() {
        var counter = 0
        var order = ArrayList<String>()

        val middleWare1 = createMiddleware<MyState, MyAction> { store, next, action ->
            counter += 1
            order.add("first")
            val nextAction = next(action)
            order.add("third")
        }

        val middleWare2 = createMiddleware<MyState, MyAction> { store, next, action ->
            counter += 1
            order.add("second")
            next(action)
        }

        val reducer = { state: MyState, action: MyAction ->
            when (action.type) {
                "hey hey!" -> MyState(state = "howdy!")
                else -> state
            }
        }

        val store = applyMiddleware(middleWare1, middleWare2)(createStore(MyState(), reducer))

        store.dispatch(MyAction(type = "hey hey!"))

        assertThat(store.state).isEqualTo(MyState("howdy!"))
        assertThat(counter).isEqualTo(2)
        assertThat(order).isEqualTo(arrayListOf("first", "second", "third"))
    }

    @Test
    fun `async middleware should be able to dispatch follow-up actions that travel through the remaining middleware`() {
        var counter = 0
        var order = ArrayList<String>()
        val testScheduler = rx.schedulers.TestScheduler()

        val fetchMiddleware = createMiddleware<MyState, MyAction> { store, next, action ->
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

        val loggerMiddleware = createMiddleware<MyState, MyAction> { store, next, action ->
            counter += 1
            order.add(action.type)
            next(action)
        }

        val reducer = { state: MyState, action: MyAction ->
            when (action.type) {
                "FETCHING" -> MyState(state = "FETCHING")
                "FETCH_COMPLETE" -> MyState(state = "FETCH_COMPLETE")
                else -> state
            }
        }

        val store = applyMiddleware(fetchMiddleware, loggerMiddleware)(createStore(MyState(), reducer))

        store.dispatch(MyAction(type = "CALL_API"))

        assertThat(counter).isEqualTo(3)
        assertThat(order).isEqualTo(arrayListOf("FETCHING", "CALL_API"))
        assertThat(store.state).isEqualTo(MyState("FETCHING"))

        testScheduler.advanceTimeBy(2L, TimeUnit.SECONDS)
        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(arrayListOf("FETCHING", "CALL_API", "FETCH_COMPLETE"))
        assertThat(store.state).isEqualTo(MyState(state = "FETCH_COMPLETE"))
    }

    @Test
    fun `async actions should be able to send new actions through the entire chain`() {
        var counter = 0
        val order = ArrayList<String>()

        val middleWare1 = createMiddleware<MyState, MyAction> { store, next, action ->
            counter += 1
            order.add("first")

            val nextAction = next(action)

            // Redispatch an action that goes through the whole chain
            // (useful for async middleware)
            if (action.type == "around!") {
                store.dispatch(MyAction());
            }
        }

        val middleWare2 = createMiddleware<MyState, MyAction> { store, next, action ->
            counter += 1
            order.add("second")
            next(action)
        }

        val reducer = { state: MyState, action: MyAction ->
            state
        }

        val store = applyMiddleware(middleWare1, middleWare2)(createStore(MyState(), reducer))

        store.dispatch(MyAction(type = "around!"))

        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(arrayListOf("first", "second", "first", "second"))
    }
}
