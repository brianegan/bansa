package com.brianegan.rxredux.counterPair

import com.brianegan.RxRedux.Store
import com.brianegan.RxRedux.createStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.schedulers.Schedulers
import java.util.*

class CounterPairReducerTest {
    @Test fun shouldInitTheCounters() {
        val applicationState = ApplicationState()
        val store = createTestStore()
        store.dispatch(INIT(applicationState))

        assertThat(store.getState()).isEqualTo(applicationState)
    }

    @Test fun shouldIncrementTheCounter() {
        val firstCounter = Pair(UUID.randomUUID(), 0)
        val store = createTestStore(createTestState(firstCounter))
        store.dispatch(INCREMENT(firstCounter.first))

        assertThat(store.getState().counters[firstCounter.first]).isEqualTo(1)
    }

    @Test fun shouldDecrementTheCounter() {
        val firstCounter = Pair(UUID.randomUUID(), 5)
        val store = createTestStore(createTestState(firstCounter))
        store.dispatch(DECREMENT(firstCounter.first))

        assertThat(store.getState().counters[firstCounter.first]).isEqualTo(4)
    }

    fun createTestStore(applicationState: ApplicationState = ApplicationState()): Store<ApplicationState, CounterAction> {
        return createStore(applicationState, counterReducer, Schedulers.immediate())
    }

    fun createTestState(firstCounter: Pair<UUID, Int> = Pair(UUID.randomUUID(), 0),
                        secondCounter: Pair<UUID, Int> = Pair(UUID.randomUUID(), 0))
            : ApplicationState {
        return ApplicationState(linkedMapOf(firstCounter, secondCounter))
    }


}
