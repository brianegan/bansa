package com.brianegan.bansa

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.observers.TestSubscriber

class StoreTest {
    data class MyState(val state: String = "initial state") : State
    data class MyAction(val type: String = "unknown") : Action

    @Test
    fun `when an action is fired, the corresponding reducer should be called and update the state of the application`() {
        val reducer = { state: MyState, action: MyAction ->
            when (action.type) {
                "to reduce" -> MyState("reduced")
                else -> state
            }
        }
        val store = createTestStore(MyState(), reducer)

        store.dispatch(MyAction(type = "to reduce"))

        assertThat(store.getState().state).isEqualTo("reduced")
    }

    @Test
    fun `when two reducers are combined, and a series of actions are fired, the correct reducer should be called`() {
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
        assertThat(store.getState().state).isEqualTo("oh hai")
        store.dispatch(MyAction(type = helloReducer2))
        assertThat(store.getState().state).isEqualTo("mark")
    }

    @Test
    fun `subscribers should be notified when the state changes`() {
        val store = createTestStore(MyState(), { state: MyState, action: MyAction -> MyState() })
        val subscriber1 = TestSubscriber.create<MyState>()
        val subscriber2 = TestSubscriber.create<MyState>()

        store.subscribe(subscriber1)
        store.subscribe(subscriber2)

        store.dispatch(MyAction())

        assertThat(subscriber1.onNextEvents.size).isGreaterThan(0)
        assertThat(subscriber2.onNextEvents.size).isGreaterThan(0)
    }

    @Test
    fun `the store should not notify unsubscribed objects`() {
        val store = createTestStore(MyState(), { state: MyState, action: MyAction -> MyState() })
        val subscriber1 = TestSubscriber.create<MyState>()
        val subscriber2 = TestSubscriber.create<MyState>()

        store.subscribe(subscriber1)
        val subscription = store.subscribe(subscriber2)
        subscription.unsubscribe()

        store.dispatch(MyAction())

        assertThat(subscriber1.onNextEvents.size).isGreaterThan(0)
        assertThat(subscriber2.onNextEvents.size).isEqualTo(0)
    }
}

