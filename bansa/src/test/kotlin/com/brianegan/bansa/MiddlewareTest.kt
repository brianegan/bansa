package com.brianegan.bansa

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Observable
import rx.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

class MiddlewareTest {
    data class TestState(val message: String = "initial state")
    data class TestAction(val type: String = "unknown") : Action

    @Test
    fun `actions should be run through a store's middleware`() {
        var counter = 0

        val reducer = Reducer<TestState> { state, action ->
            state
        }

        val middleWare = Middleware<TestState> { store, action, next ->
            counter += 1
            next.dispatch(action)
        }

        val store = BaseStore(TestState(), reducer, middleWare)

        store.dispatch(TestAction(type = "hey hey!"))

        assertThat(store.state).isEqualTo(TestState())
        assertThat(counter).isEqualTo(1)
    }

    @Test
    fun `actions should pass through the middleware chain in the correct order`() {
        var counter = 0
        var order = mutableListOf<String>()

        val middleWare1 = Middleware<TestState> { store, action, next ->
            counter += 1
            order.add("first")
            val nextAction = next.dispatch(action)
            order.add("third")
        }

        val middleWare2 = Middleware<TestState> { store, action, next ->
            counter += 1
            order.add("second")
            next.dispatch(action)
        }

        val reducer = Reducer<TestState> { state, action ->
            when (action) {
                is TestAction -> when (action.type) {
                    "hey hey!" -> TestState(message = "howdy!")
                    else -> state
                }
                else -> state
            }
        }

        val store = BaseStore(TestState(), reducer, middleWare1, middleWare2)

        store.dispatch(TestAction(type = "hey hey!"))

        assertThat(store.state).isEqualTo(TestState("howdy!"))
        assertThat(counter).isEqualTo(2)
        assertThat(order).isEqualTo(listOf("first", "second", "third"))
    }

    @Test
    fun `async middleware should be able to dispatch follow-up actions that travel through the remaining middleware`() {
        var counter = 0
        var order = mutableListOf<String>()
        val testScheduler = TestScheduler()

        val fetchMiddleware = Middleware<TestState> { store, action, next ->
            counter += 1
            when (action) {
                is TestAction -> when (action.type) {
                    "CALL_API" -> {
                        next.dispatch(TestAction("FETCHING"))
                        Observable
                                .just(5)
                                .delay(1L, TimeUnit.SECONDS, testScheduler)
                                .subscribe({
                                               next.dispatch(TestAction("FETCH_COMPLETE"))
                                           })

                        next.dispatch(action)
                    }
                    else -> next.dispatch(action)
                }
                else -> next.dispatch(action)
            }
        }

        val loggerMiddleware = Middleware<TestState> { store, action, next ->
            counter += 1
            order.add((action as TestAction).type)
            next.dispatch(action)
        }

        val reducer = Reducer<TestState> { state, action ->
            when (action) {
                is TestAction -> when (action.type) {
                    "FETCHING" -> TestState(message = "FETCHING")
                    "FETCH_COMPLETE" -> TestState(message = "FETCH_COMPLETE")
                    else -> state
                }
                else -> state
            }
        }

        val store = BaseStore(TestState(), reducer, fetchMiddleware, loggerMiddleware)

        store.dispatch(TestAction(type = "CALL_API"))

        assertThat(counter).isEqualTo(3)
        assertThat(order).isEqualTo(listOf("FETCHING", "CALL_API"))
        assertThat(store.state).isEqualTo(TestState("FETCHING"))

        testScheduler.advanceTimeBy(2L, TimeUnit.SECONDS)
        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(listOf("FETCHING", "CALL_API", "FETCH_COMPLETE"))
        assertThat(store.state).isEqualTo(TestState(message = "FETCH_COMPLETE"))
    }

    @Test
    fun `async actions should be able to send new actions through the entire chain`() {
        var counter = 0
        val order = mutableListOf<String>()

        val middleWare1 = Middleware<TestState> { store, action, next ->
            counter += 1
            order.add("first")

            val nextAction = next.dispatch(action)

            // Redispatch an action that goes through the whole chain
            // (useful for async middleware)
            if ((action as TestAction).type == "around!") {
                store.dispatch(TestAction());
            }
        }

        val middleWare2 = Middleware<TestState> { store, action, next ->
            counter += 1
            order.add("second")
            next.dispatch(action)
        }

        val reducer = Reducer<TestState> { state, action ->
            state
        }

        val store = BaseStore(TestState(), reducer, middleWare1, middleWare2)

        store.dispatch(TestAction(type = "around!"))

        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(listOf("first", "second", "first", "second"))
    }
}
