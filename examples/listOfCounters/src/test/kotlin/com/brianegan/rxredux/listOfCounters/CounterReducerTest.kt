package com.brianegan.rxredux.listOfCounters

import com.brianegan.RxRedux.Store
import com.brianegan.RxRedux.createStore
import com.github.andrewoma.dexx.kollection.immutableListOf
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import rx.schedulers.Schedulers

class CounterListReducerTest: Spek() { init {
    given("an app that's booting up") {
        val store = createTestStore()

        on("app firing init with a value") {
            val initialState = ApplicationState()
            store.dispatch(INIT(initialState))

            it("should initialize the app with a given state") {
                assertThat(store.getState()).isEqualTo(initialState)
            }
        }
    }

    given("an app with one counter") {
        val counter = Counter(value = 0)
        val initialState = ApplicationState(immutableListOf(counter))
        val store = createTestStore(initialState)

        on("app firing the increment action on a counter") {
            store.dispatch(INCREMENT(counter.id))

            it("should increase the value of the counter by 1") {
                assertThat(store.getState().counters.first().value).isEqualTo(1)
            }
        }
    }

    given("an app with one counter") {
        val counter = Counter(value = 0)
        val initialState = ApplicationState(immutableListOf(counter))
        val store = createTestStore(initialState)

        on("app firing the decrement action on a counter") {
            store.dispatch(DECREMENT(counter.id))

            it("should decrease the value of the counter by 1") {
                assertThat(store.getState().counters.first().value).isEqualTo(-1)
            }
        }
    }

    given("an app with a few counters") {
        val counter1 = Counter()
        val counter2 = Counter()
        val counter3 = Counter()
        val initialState = ApplicationState(
                immutableListOf(counter1, counter2, counter3))
        val store = createTestStore(initialState)

        on("app firing the add counter action") {
            val counter4 = Counter()
            store.dispatch(ADD(counter4))

            it("should append the counter to the end of the list") {
                assertThat(store.getState().counters.size).isEqualTo(4)
                assertThat(store.getState().counters.last()).isEqualTo(counter4)
            }
        }
    }

    given("an app with a few counters") {
        val counter1 = Counter()
        val counter2 = Counter()
        val counter3 = Counter()
        val initialState = ApplicationState(
                immutableListOf(counter1, counter2, counter3))
        val store = createTestStore(initialState)

        on("app firing the remove counter action") {
            store.dispatch(REMOVE)

            it("should remove the counter from the end of the list") {
                assertThat(store.getState().counters.size).isEqualTo(2)
            }
        }
    }
}}

fun createTestStore(initialState: ApplicationState = ApplicationState()): Store<ApplicationState, CounterAction> {
    return createStore(initialState, counterReducer, Schedulers.immediate())
}
