package com.brianegan.betterreads

import com.brianegan.RxRedux.Store
import com.brianegan.RxRedux.createStore
import com.brianegan.listOfCounters.*
import com.github.andrewoma.dexx.kollection.ImmutableList
import com.github.andrewoma.dexx.kollection.immutableListOf
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.shouldEqual
import rx.schedulers.Schedulers

class CounterReducerTest : Spek() {
    init {
        given("An application store") {
            val store = createTestStore()

            on("initializing the app") {
                store.dispatch(INIT)

                it("should start with a sensible default") {
                    shouldEqual(ApplicationState(), store.getState())
                }
            }
        }
    }

    fun createTestStore(): Store<ApplicationState, CounterAction> =
            createStore(ApplicationState(createTestCounters()), counterReducer, Schedulers.immediate())

    fun createTestCounters(): ImmutableList<Counter> =
            immutableListOf(Counter(), Counter(), Counter())
}
