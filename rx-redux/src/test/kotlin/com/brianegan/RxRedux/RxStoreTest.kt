package com.brianegan.RxRedux

import rx.observers.TestSubscriber
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class RxStoreTest {
    data class MyState(val state: String = "initial state") : State
    data class MyAction(val type: String = "unknown") : Action

    @test fun actionShouldBeReduced() {
        val reducer = { state: MyState, action: MyAction ->
            when (action.type) {
                "to reduce" -> MyState("reduced")
                else -> state
            }
        }

        val store = createTestStore(MyState(), reducer)
        store.dispatch(MyAction(type = "to reduce"))
        assertEquals("reduced", store.getState().state)
    }

    @test fun combineReducersShouldSmashTwoReducersTogether() {
        val helloReducer1 = "helloReducer1"
        val helloReducer2 = "helloReducer2"

        val reducer1 = { state: MyState, action: MyAction ->
            when (action.type) {
                helloReducer1 -> MyState("oh hai")
                else -> state
            }
        }

        val reducer2 = { state: MyState, action: MyAction ->
            when (action.type) {
                helloReducer2 -> MyState("mark")
                else -> state
            }
        }

        val store = createTestStore(MyState(), combineReducers(reducer1, reducer2))

        store.dispatch(MyAction(type = helloReducer1))
        assertEquals(store.getState().state, "oh hai")
        store.dispatch(MyAction(type = helloReducer2))
        assertEquals(store.getState().state, "mark")
    }

    @test fun storeShouldNotifySubscribers() {
        val store = createStore(MyState(), { state: MyState, action: MyAction -> MyState() })
        val subscriber1 = TestSubscriber.create<MyState>()
        val subscriber2 = TestSubscriber.create<MyState>()

        store.subscribe(subscriber1)
        store.subscribe(subscriber2)
        store.dispatch(MyAction())

        assertTrue(subscriber1.onNextEvents.size > 0)
        assertTrue(subscriber2.onNextEvents.size > 0)
    }

    @test fun storeShouldNotNotifyWhenUnsubscribed() {
        val store = createStore(MyState(), { state: MyState, action: MyAction -> MyState() })
        val subscriber1 = TestSubscriber.create<MyState>()
        val subscriber2 = TestSubscriber.create<MyState>()

        store.subscribe(subscriber1)
        val subscription = store.subscribe(subscriber2)
        subscription.unsubscribe()
        store.dispatch(MyAction())

        assertTrue(subscriber1.onNextEvents.size > 0)
        assertTrue(subscriber2.onNextEvents.size == 0)
    }
}
