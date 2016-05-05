package com.brianegan.bansaKotlin

import com.brianegan.bansa.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KotlinExtensionsTest {
    data class MyState(val state: String = "initial state")
    data class MyAction(val type: String = "unknown") : Action

    val EXPECTED_STATE = "modified state"

    @Test
    fun `reducers should be callable as a function`() {
        val initialState = MyState();
        val expectedState = initialState.copy(EXPECTED_STATE)
        val reducer = Reducer<MyState> { state, action ->
            state.copy(EXPECTED_STATE)
        }

        assertThat(reducer(initialState, MyAction())).isEqualTo(expectedState);
    }

    @Test
    fun `middleware should be callable as a function`() {
        var called = false;
        val next = NextDispatcher { }
        val reducer = Reducer<MyState> { state, action ->
            state.copy(EXPECTED_STATE)
        }

        val middleware = Middleware<MyState> { store, action, next ->
            called = true;
        }

        val store = BaseStore<MyState>(MyState(), reducer);

        middleware(store, MyAction(), next)

        assertThat(called).isTrue()
    }

    @Test
    fun `next dispatchers should be callable as a function`() {
        var called = false;
        val next = NextDispatcher { called = true }

        next(MyAction())

        assertThat(called).isTrue()
    }
}
