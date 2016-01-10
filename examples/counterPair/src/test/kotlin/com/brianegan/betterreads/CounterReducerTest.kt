package com.brianegan.betterreads

import com.brianegan.RxRedux.Store
import com.brianegan.RxRedux.createStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.schedulers.Schedulers

class CounterReducerTest {
    @Test fun shouldInitTheCounter() {
        val store = createTestStore()
        store.dispatch(CounterActions.INIT)

        assertThat(store.getState()).isEqualTo(ApplicationState())
    }

    @Test fun shouldIncrementTheCounter() {
        val store = createTestStore()
        store.dispatch(CounterActions.INCREMENT)

        assertThat(store.getState()).isEqualTo(ApplicationState(1))
    }

    @Test fun shouldDecrementTheCounter() {
        val store = createTestStore()
        store.dispatch(CounterActions.DECREMENT)

        assertThat(store.getState()).isEqualTo(ApplicationState(-1))
    }

    fun createTestStore(): Store<ApplicationState, CounterAction> =
            createStore(ApplicationState(), counterReducer, Schedulers.immediate())
}
