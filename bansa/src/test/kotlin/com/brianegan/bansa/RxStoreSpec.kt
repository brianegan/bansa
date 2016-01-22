package com.brianegan.bansa

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import rx.observers.TestSubscriber
import org.junit.Test as test

class RxStoreSpec : Spek() {
    data class MyState(val state: String = "initial state") : State
    data class MyAction(val type: String = "unknown") : Action

    init {
        given("a reducer") {
            val reducer = { state: MyState, action: MyAction ->
                when (action.type) {
                    "to reduce" -> MyState("reduced")
                    else -> state
                }
            }
            val store = createTestStore(MyState(), reducer)

            on("action fired that matches the reducer") {
                store.dispatch(MyAction(type = "to reduce"))

                it("should run the reducing function, updating the store") {
                    assertThat(store.getState().state).isEqualTo("reduced")
                }
            }
        }

        given("two combined reducers") {
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

            on("series of actions fired") {
                it("should match the correct reducer") {
                    store.dispatch(MyAction(type = helloReducer1))
                    assertThat(store.getState().state).isEqualTo("oh hai")
                    store.dispatch(MyAction(type = helloReducer2))
                    assertThat(store.getState().state).isEqualTo("mark")
                }
            }
        }

        given("a store with subscribers") {
            val store = createTestStore(MyState(), { state: MyState, action: MyAction -> MyState() })
            val subscriber1 = TestSubscriber.create<MyState>()
            val subscriber2 = TestSubscriber.create<MyState>()

            store.subscribe(subscriber1)
            store.subscribe(subscriber2)

            on("action fired") {
                store.dispatch(MyAction())

                it("should notify all subscriberrs") {
                    assertThat(subscriber1.onNextEvents.size).isGreaterThan(0)
                    assertThat(subscriber2.onNextEvents.size).isGreaterThan(0)
                }
            }
        }

        given("a store with unsubscribed subscribers") {
            val store = createTestStore(MyState(), { state: MyState, action: MyAction -> MyState() })
            val subscriber1 = TestSubscriber.create<MyState>()
            val subscriber2 = TestSubscriber.create<MyState>()

            store.subscribe(subscriber1)
            val subscription = store.subscribe(subscriber2)
            subscription.unsubscribe()

            on("action fired") {
                store.dispatch(MyAction())

                it("should only notify the remaining subscribers") {
                    assertThat(subscriber1.onNextEvents.size).isGreaterThan(0)
                    assertThat(subscriber2.onNextEvents.size).isEqualTo(0)
                }
            }
        }
    }
}
