package com.brianegan.bansa.listOfCountersVariant

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.brianegan.bansa.Store
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import trikita.anvil.recyclerview.v7.RecyclerViewv7DSL.*

class RootView(c: Context, val store: Store<ApplicationState, CounterAction>) : RenderableView(c) {
    val stateChangeSubscription = store.subscribe { Anvil.render() }

    val mapCounterToViewModel = buildMapCounterToCounterViewModel(store)

    val adapter: BansaRenderableRecyclerViewAdapter<Counter, CounterViewModel> = BansaRenderableRecyclerViewAdapter(
            mapCounterToViewModel,
            ::counterView,
            { models, pos ->
                models[pos].id.leastSignificantBits
            }, true
    )

    val add: (View) -> Unit = {
        store.dispatch(ADD())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateChangeSubscription.unsubscribe()
    }

    override fun view() {
        frameLayout {
            button {
                init {
                    size(FILL, dip(50))
                    text("Add")
                    padding(dip(10))
                }

                onClick(add)
            }

            recyclerView {
                init {
                    layoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
                    itemAnimator(DefaultItemAnimator())
                    hasFixedSize(false)
                    margin(dip(0), dip(50), dip(0), dip(0))
                    size(FILL, FILL)
                }

                adapter(adapter.update(store.state.counters))
            }
        }
    }
}
