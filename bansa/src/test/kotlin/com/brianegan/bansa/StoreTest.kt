package com.brianegan.bansaDevTools

import com.brianegan.bansa.BansaStore
import com.brianegan.bansa.BansaUtils.combineReducers
import com.brianegan.bansa.Reducer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StoreTest {
    data class MyState(val message: String = "initial state")
    data class MyAction(val type: String = "unknown")

    @Test
    fun `when an action is fired, the corresponding reducer should be called and update the state of the application`() {
        val reducer = Reducer<MyState, MyAction> { state, action ->
            when (action.type) {
                "to invoke" -> MyState("reduced")
                else -> state
            }
        }

        val store = BansaStore(MyState(), reducer)

        store.dispatch(MyAction(type = "to invoke"))

        assertThat(store.state.message).isEqualTo("reduced")
    }

    @Test
    fun `when two reducers are combined, and a series of actions are fired, the correct reducer should be called`() {
        val helloReducer1 = "helloReducer1"
        val helloReducer2 = "helloReducer2"

        val reducer1 = Reducer<MyState, MyAction> { state, action ->
            when (action.type) {
                helloReducer1 -> MyState("oh hai")
                else -> state
            }
        }

        val reducer2 = Reducer<MyState, MyAction> { state, action ->
            when (action.type) {
                helloReducer2 -> MyState("mark")
                else -> state
            }
        }

        val store = BansaStore(MyState(), combineReducers(reducer1, reducer2))

        store.dispatch(MyAction(type = helloReducer1))
        assertThat(store.state.message).isEqualTo("oh hai")
        store.dispatch(MyAction(type = helloReducer2))
        assertThat(store.state.message).isEqualTo("mark")
    }

    @Test
    fun `subscribers should be notified when the state changes`() {
        val store = BansaStore(MyState(), Reducer<MyState, MyAction> { state, action -> MyState() })
        var subscriber1Called = false
        var subscriber2Called = false

        store.subscribe { subscriber1Called = true }
        store.subscribe { subscriber2Called = true }

        store.dispatch(MyAction())

        assertThat(subscriber1Called).isTrue()
        assertThat(subscriber2Called).isTrue()
    }

    @Test
    fun `the store should not notify unsubscribed objects`() {
        val store = BansaStore(MyState(), Reducer<MyState, MyAction> { state, action -> MyState() })
        var subscriber1Called = false
        var subscriber2Called = false

        store.subscribe { subscriber1Called = true }
        val subscription = store.subscribe { subscriber2Called = true }
        subscription.unsubscribe()

        store.dispatch(MyAction())

        assertThat(subscriber1Called).isTrue()
        assertThat(subscriber2Called).isFalse()
    }

    @Test
    fun `store should pass the current state to subscribers`() {
        val reducer = Reducer<MyState, MyAction> { state, action ->
            when (action.type) {
                "to invoke" -> MyState("reduced")
                else -> state
            }
        }

        var actual: MyState = MyState()
        val store = BansaStore(MyState(), reducer)

        store.subscribe { actual = it }
        store.dispatch(MyAction(type = "to invoke"))

        assertThat(actual).isEqualTo(store.state)
    }
}

