package com.brianegan.bansaDevTools

import com.brianegan.bansa.Action
import com.brianegan.bansa.Middleware
import com.brianegan.bansa.Reducer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Observable
import rx.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

class DevToolsMiddlewareTest {
    data class TestState(val message: String = "initial state")
    data class TestAction(val type: String = "unknown") : Action
    object HeyHey : Action
    object CallApi : Action
    object Fetching : Action
    object FetchComplete : Action
    object Around : Action

    @Test fun `unwrapped actions should be run through a store's middleware`() {
        var counter = 0

        val reducer = Reducer<TestState> { state, action ->
            state.copy("Reduced?")
        }

        val middleWare = Middleware<TestState> { store, action, next ->
            counter += 1
            next.dispatch(action)
        }

        val store = DevToolsStore(TestState(), reducer, middleWare)

        store.dispatch(TestAction())

        assertThat(counter).isEqualTo(2)
        assertThat(store.state.message).isEqualTo("Reduced?")
    }

    @Test
    fun `unwrapped actions should pass through the middleware chain in the correct order`() {
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
                is HeyHey -> TestState(message = "howdy!")
                else -> state
            }
        }

        val store = DevToolsStore(TestState(), reducer, middleWare1, middleWare2)

        store.dispatch(HeyHey)

        assertThat(store.state).isEqualTo(TestState("howdy!"))
        assertThat(counter).isEqualTo(4)
        assertThat(order).isEqualTo(listOf("first", "second", "third", "first", "second", "third"))
    }

    @Test
    fun `async middleware should be able to dispatch follow-up unwrapped actions that travel through the remaining middleware`() {
        var counter = 0
        var order = mutableListOf<String>()
        val testScheduler = TestScheduler()

        val fetchMiddleware = Middleware<TestState> { store, action, next ->
            counter += 1
            when (action) {
                is CallApi -> {
                    next.dispatch(Fetching)
                    Observable
                            .just(5)
                            .delay(1L, TimeUnit.SECONDS, testScheduler)
                            .subscribe({
                                next.dispatch(FetchComplete)
                            })

                    next.dispatch(action)
                }
                else -> next.dispatch(action)
            }
        }

        val loggerMiddleware = Middleware<TestState> { store, action, next ->
            counter += 1
            when (action) {
                is CallApi -> order.add("CALL_API")
                is Fetching -> order.add("FETCHING")
                is FetchComplete -> order.add("FETCH_COMPLETE")
            }

            next.dispatch(action)
        }

        val reducer = Reducer<TestState> { state, action ->
            when (action) {
                Fetching -> TestState(message = "FETCHING")
                FetchComplete -> TestState(message = "FETCH_COMPLETE")
                else -> state
            }
        }

        val store = DevToolsStore(TestState(), reducer, fetchMiddleware, loggerMiddleware)

        store.dispatch(CallApi)

        assertThat(counter).isEqualTo(5)
        assertThat(order).isEqualTo(listOf("FETCHING", "CALL_API"))
        assertThat(store.state).isEqualTo(TestState("FETCHING"))

        testScheduler.advanceTimeBy(2L, TimeUnit.SECONDS)
        assertThat(counter).isEqualTo(6)
        assertThat(order).isEqualTo(listOf("FETCHING", "CALL_API", "FETCH_COMPLETE"))
        assertThat(store.state).isEqualTo(TestState(message = "FETCH_COMPLETE"))
    }

    @Test
    fun `async actions should be able to send new unwrapped actions through the entire chain`() {
        var counter = 0
        val order = mutableListOf<String>()

        val middleWare1 = Middleware<TestState> { store, action, next ->
            counter += 1
            order.add("first")

            val nextAction = next.dispatch(action)

            // Redispatch an action that goes through the whole chain
            // (useful for async middleware)
            if (action is Around) {
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

        val store = DevToolsStore(TestState(), reducer, middleWare1, middleWare2)

        store.dispatch(Around)

        assertThat(counter).isEqualTo(6)
        assertThat(order).isEqualTo(listOf("first", "second", "first", "second", "first", "second"))
    }
}
