package com.brianegan.bansa.listOfCounters

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brianegan.bansa.Store
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableAdapter
import trikita.anvil.RenderableView

class RootView(c: Context, val store: Store<ApplicationState, CounterAction>) : RenderableView(c) {
    val stateChangeSubscription = store.subscribe { Anvil.render() }

    val mapCounterToViewModel = buildMapCounterToCounterViewModel(store)

    val adapter: BansaAdapter<Counter, CounterViewModel> = BansaAdapter(
            listOf(),
            mapCounterToViewModel,
            RenderableAdapter.Item { i, vm -> counterView(vm) }
    )

    val add: (View) -> Unit = {
        store.dispatch(ADD())
    }

    val remove: (View) -> Unit = {
        store.dispatch(REMOVE)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateChangeSubscription.unsubscribe()
    }

    override fun view() {
        frameLayout {
            linearLayout {
                orientation(LinearLayout.VERTICAL)
                size(FILL, dip(50))

                linearLayout {
                    orientation(LinearLayout.HORIZONTAL)

                    button {
                        size(0, WRAP)
                        weight(1f)
                        text("Add")
                        padding(dip(10))
                        onClick(add)
                    }

                    button {
                        size(0, WRAP)
                        weight(1f)
                        text("Remove")
                        padding(dip(10))
                        onClick(remove)
                    }
                }
            }

            listView {
                margin(dip(0), dip(50), dip(0), dip(0))
                size(FILL, FILL)
                adapter(adapter.update(store.state.counters))
            }
        }
    }
}
