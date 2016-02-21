package com.brianegan.bansa.counter

import com.brianegan.bansa.Store
import com.brianegan.bansa.createStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.schedulers.Schedulers

class CounterReducerTest {
    @Test fun `INIT action should initialize the counter`() {
        val store = createTestStore()
        store.dispatch(CounterActions.INIT)

        assertThat(store.getState()).isEqualTo(ApplicationState())
    }

    @Test fun `INCREMENT action should increment the counter state`() {
        val store = createTestStore()
        store.dispatch(CounterActions.INCREMENT)

        assertThat(store.getState()).isEqualTo(ApplicationState(1))
    }

    @Test fun `DECREMENT action should decrement the counter state`() {
        val store = createTestStore()
        store.dispatch(CounterActions.DECREMENT)

        assertThat(store.getState()).isEqualTo(ApplicationState(-1))
    }

    fun createTestStore(): Store<ApplicationState, CounterAction> =
            createStore(ApplicationState(), counterReducer, Schedulers.immediate())
}
