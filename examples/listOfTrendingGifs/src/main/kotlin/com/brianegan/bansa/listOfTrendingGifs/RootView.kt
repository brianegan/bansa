package com.brianegan.bansa.listOfTrendingGifs

import android.content.Context
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.actions.FETCH_NEXT_PAGE
import com.brianegan.bansa.listOfTrendingGifs.actions.REFRESH
import com.brianegan.bansa.listOfTrendingGifs.models.Gif
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansa.listOfTrendingGifs.ui.AnvilSwipeRefreshLayout.*
import com.brianegan.bansa.listOfTrendingGifs.ui.gifView
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.BansaAdapter
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.OnScrolledToEndOfListListener
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.subscriptions.Subscriptions
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class RootView(c: Context, val store: Store<ApplicationState, Any>) : RenderableView(c) {
    override fun view() {
        swipeRefreshLayout {
            onRefresh { store.dispatch(REFRESH) }
            refreshing(store.getState().isRefreshing)

            listView {
                size(FILL, FILL)
                dividerHeight(0)
                adapter(adapter.update(store.getState().gifs))
                onScroll(fetchMoreGifsAtEndOfListListener)
            }
        }
    }

    val identity = { it: Gif -> it }

    val adapter: BansaAdapter<Gif, Gif> = BansaAdapter(
            store.getState().gifs,
            identity,
            ::gifView
    )

    val fetchMoreGifsAtEndOfListListener = OnScrolledToEndOfListListener(
            { store.getState().isFetching.not() },
            { store.dispatch(FETCH_NEXT_PAGE) })

    var subscription: Subscription = Subscriptions.empty()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        store.dispatch(REFRESH)

        subscription = store.stateChanges.observeOn(AndroidSchedulers.mainThread()).subscribe(Action1 {
            Anvil.render()
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription.unsubscribe()
    }
}
