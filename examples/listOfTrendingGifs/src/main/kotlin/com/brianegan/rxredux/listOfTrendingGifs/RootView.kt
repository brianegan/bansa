package com.brianegan.rxredux.listOfTrendingGifs

import android.content.Context
import android.widget.AbsListView
import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import com.brianegan.rxredux.listOfCounters.ReduxAdapter
import com.brianegan.rxredux.listOfCounters.gifView
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.subscriptions.Subscriptions
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

public class RootView(c: Context, val store: Store<ApplicationState, Action>) : RenderableView(c) {
    val FETCH_THRESHOLD = 10

    override fun view() {
        listView {
            size(FILL, FILL)
            adapter(adapter.update(store.getState().gifs))
            onScroll(object : AbsListView.OnScrollListener {
                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

                }

                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                    val lastVisibleItem = firstVisibleItem.plus(visibleItemCount);
                    if (store.getState().isFetching.not() && lastVisibleItem == totalItemCount.minus(FETCH_THRESHOLD)) {
                        store.dispatch(FETCH_NEXT_PAGE)
                    }
                }
            })
        }
    }

    val identity = { it: Gif -> it }

    val adapter: ReduxAdapter<Gif, Gif> = ReduxAdapter(
            store.getState().gifs,
            identity,
            ::gifView
    )

    var subscription: Subscription = Subscriptions.empty()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        store.dispatch(REFRESH)

        subscription = store.state.observeOn(AndroidSchedulers.mainThread()).subscribe(Action1 {
            Anvil.render()
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription.unsubscribe()
    }
}
