package com.brianegan.bansaDevTools

import com.brianegan.bansa.BaseStore
import com.brianegan.bansa.Middleware
import com.brianegan.bansa.Reducer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Observable
import rx.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

class MiddlewareTest {
    data class MyState(val state: String = "initial state")
    data class MyAction(val type: String = "unknown")

    @Test
    fun `actions should be run through a store's middleware`() {
        var counter = 0

        val reducer = Reducer<MyState, MyAction> { state, action ->
            state
        }

        val middleWare = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            next.dispatch(action)
        }

        val store = BaseStore(MyState(), reducer, middleWare)

        store.dispatch(MyAction(type = "hey hey!"))

        assertThat(store.state).isEqualTo(MyState())
        assertThat(counter).isEqualTo(1)
    }

    @Test
    fun `actions should pass through the middleware chain in the correct order`() {
        var counter = 0
        var order = mutableListOf<String>()

        val middleWare1 = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            order.add("first")
            val nextAction = next.dispatch(action)
            order.add("third")
        }

        val middleWare2 = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            order.add("second")
            next.dispatch(action)
        }

        val reducer = Reducer<MyState, MyAction> { state, action ->
            when (action.type) {
                "hey hey!" -> MyState(state = "howdy!")
                else -> state
            }
        }

        val store = BaseStore(MyState(), reducer, middleWare1, middleWare2)

        store.dispatch(MyAction(type = "hey hey!"))

        assertThat(store.state).isEqualTo(MyState("howdy!"))
        assertThat(counter).isEqualTo(2)
        assertThat(order).isEqualTo(listOf("first", "second", "third"))
    }

    @Test
    fun `async middleware should be able to dispatch follow-up actions that travel through the remaining middleware`() {
        var counter = 0
        var order = mutableListOf<String>()
        val testScheduler = TestScheduler()

        val fetchMiddleware = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            when (action.type) {
                "CALL_API" -> {
                    next.dispatch(MyAction("FETCHING"))
                    Observable
                            .just(5)
                            .delay(1L, TimeUnit.SECONDS, testScheduler)
                            .subscribe({
                                next.dispatch(MyAction("FETCH_COMPLETE"))
                            })

                    next.dispatch(action)
                }
                else -> next.dispatch(action)
            }
        }

        val loggerMiddleware = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            order.add(action.type)
            next.dispatch(action)
        }

        val reducer = Reducer<MyState, MyAction> { state, action ->
            when (action.type) {
                "FETCHING" -> MyState(state = "FETCHING")
                "FETCH_COMPLETE" -> MyState(state = "FETCH_COMPLETE")
                else -> state
            }
        }

        val store = BaseStore(MyState(), reducer, fetchMiddleware, loggerMiddleware)

        store.dispatch(MyAction(type = "CALL_API"))

        assertThat(counter).isEqualTo(3)
        assertThat(order).isEqualTo(listOf("FETCHING", "CALL_API"))
        assertThat(store.state).isEqualTo(MyState("FETCHING"))

        testScheduler.advanceTimeBy(2L, TimeUnit.SECONDS)
        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(listOf("FETCHING", "CALL_API", "FETCH_COMPLETE"))
        assertThat(store.state).isEqualTo(MyState(state = "FETCH_COMPLETE"))
    }

    @Test
    fun `async actions should be able to send new actions through the entire chain`() {
        var counter = 0
        val order = mutableListOf<String>()

        val middleWare1 = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            order.add("first")

            val nextAction = next.dispatch(action)

            // Redispatch an action that goes through the whole chain
            // (useful for async middleware)
            if (action.type == "around!") {
                store.dispatch(MyAction());
            }
        }

        val middleWare2 = Middleware<MyState, MyAction> { store, action, next ->
            counter += 1
            order.add("second")
            next.dispatch(action)
        }

        val reducer = Reducer<MyState, MyAction> { state, action ->
            state
        }

        val store = BaseStore(MyState(), reducer, middleWare1, middleWare2)

        store.dispatch(MyAction(type = "around!"))

        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(listOf("first", "second", "first", "second"))
    }
}
