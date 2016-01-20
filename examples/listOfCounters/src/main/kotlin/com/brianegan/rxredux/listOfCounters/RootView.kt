package com.brianegan.rxredux.listOfCounters

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableAdapter
import trikita.anvil.RenderableView

public class RootView(c: Context, val store: Store<ApplicationState, Action>) : RenderableView(c) {
    val stateChangeSubscription: Subscription = store
            .state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Action1 { Anvil.render() })

    val mapCounterToViewModel = buildMapCounterToCounterViewModel(store)

    val adapter: ReduxAdapter<Counter, CounterViewModel> = ReduxAdapter(
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
                adapter(adapter.update(store.getState().counters))
            }
        }
    }
}
