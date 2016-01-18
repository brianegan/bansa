package com.brianegan.listOfCounters

import android.content.Context
import android.view.View
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
            // Yay! We can easily schedule when we perform view updates as to not
            // cause too much churn on the view layer in response to rapid state changes,
            // which could diminish app performance
            .observeOn(AndroidSchedulers.mainThread())
            // This is where the magic happens. The Root view subscribes to state changes,
            // and triggers the Anvil library to re-render the app with the current state.
            .subscribe(Action1 { Anvil.render() })

    val mapCounterToViewModel = buildMapCounterToCounterViewModel(store)

    val adapter: ReduxAdapter<Counter, CounterViewModel> = ReduxAdapter(
            mapCounterToViewModel,
            RenderableAdapter.Item { i, vm -> counterView(vm) }
    )

    val add: (View) -> Unit = {
        store.dispatch(ADD)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateChangeSubscription.unsubscribe()
    }

    override fun view() {
        frameLayout {
            button {
                size(FILL, dip(50))
                text("Add")
                padding(dip(10))
                onClick(add)
            }

            listView {
                margin(dip(0), dip(50), dip(0), dip(0))
                size(FILL, FILL)
                adapter(adapter.update(store.getState().counters))
            }
        }
    }
}
