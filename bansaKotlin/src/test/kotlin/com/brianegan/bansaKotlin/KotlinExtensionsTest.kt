package com.brianegan.bansaKotlin

import com.brianegan.bansa.BaseStore
import com.brianegan.bansa.Middleware
import com.brianegan.bansa.NextDispatcher
import com.brianegan.bansa.Reducer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KotlinExtensionsTest {
    data class MyState(val state: String = "initial state")
    data class MyAction(val type: String = "unknown")

    val EXPECTED_STATE = "modified state"

    @Test
    fun `reducers should be callable as a function`() {
        val initialState = MyState();
        val expectedState = initialState.copy(EXPECTED_STATE)
        val reducer = Reducer<MyState, MyAction> { state, action ->
            state.copy(EXPECTED_STATE)
        }

        assertThat(reducer(initialState, MyAction())).isEqualTo(expectedState);
    }

    @Test
    fun `middleware should be callable as a function`() {
        var called = false;
        val next = NextDispatcher<MyAction> { }
        val reducer = Reducer<MyState, MyAction> { state, action ->
            state.copy(EXPECTED_STATE)
        }

        val middleware = Middleware<MyState, MyAction> { store, action, next ->
            called = true;
        }

        val store = BaseStore<MyState, MyAction>(MyState(), reducer);

        middleware(store, MyAction(), next)

        assertThat(called).isTrue()
    }

    @Test
    fun `next dispatchers should be callable as a function`() {
        var called = false;
        val next = NextDispatcher<MyAction> { called = true }

        next(MyAction())

        assertThat(called).isTrue()
    }
}
