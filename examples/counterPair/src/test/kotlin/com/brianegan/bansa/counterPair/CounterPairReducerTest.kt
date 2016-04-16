package com.brianegan.bansa.counterPair

import com.brianegan.bansaKotlin.Store
import com.brianegan.bansa.createStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class CounterPairReducerTest {
    @Test fun shouldInitTheCounters() {
        val applicationState = ApplicationState()
        val store = createTestStore()
        store.dispatch(INIT(applicationState))

        assertThat(store.state).isEqualTo(applicationState)
    }

    @Test fun shouldIncrementTheCounter() {
        val firstCounter = Pair(UUID.randomUUID(), 0)
        val store = createTestStore(createTestState(firstCounter))
        store.dispatch(INCREMENT(firstCounter.first))

        assertThat(store.state.counters[firstCounter.first]).isEqualTo(1)
    }

    @Test fun shouldDecrementTheCounter() {
        val firstCounter = Pair(UUID.randomUUID(), 5)
        val store = createTestStore(createTestState(firstCounter))
        store.dispatch(DECREMENT(firstCounter.first))

        assertThat(store.state.counters[firstCounter.first]).isEqualTo(4)
    }

    fun createTestStore(applicationState: ApplicationState = ApplicationState()): Store<ApplicationState, CounterAction> {
        return createStore(applicationState, counterReducer)
    }

    fun createTestState(firstCounter: Pair<UUID, Int> = Pair(UUID.randomUUID(), 0),
                        secondCounter: Pair<UUID, Int> = Pair(UUID.randomUUID(), 0))
            : ApplicationState {
        return ApplicationState(linkedMapOf(firstCounter, secondCounter))
    }


}
