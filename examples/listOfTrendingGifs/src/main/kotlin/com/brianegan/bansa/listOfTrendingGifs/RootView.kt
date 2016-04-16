package com.brianegan.bansa.listOfTrendingGifs

import android.content.Context
import com.brianegan.bansa.Store
import com.brianegan.bansa.Subscription
import com.brianegan.bansa.listOfTrendingGifs.actions.FETCH_NEXT_PAGE
import com.brianegan.bansa.listOfTrendingGifs.actions.REFRESH
import com.brianegan.bansa.listOfTrendingGifs.models.Gif
import com.brianegan.bansa.listOfTrendingGifs.state.ApplicationState
import com.brianegan.bansa.listOfTrendingGifs.ui.AnvilSwipeRefreshLayout.*
import com.brianegan.bansa.listOfTrendingGifs.ui.gifView
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.BansaAdapter
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.OnScrolledToEndOfListListener
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class RootView(c: Context, val store: Store<ApplicationState, Any>) : RenderableView(c) {
    override fun view() {
        swipeRefreshLayout {
            onRefresh { store.dispatch(REFRESH) }
            refreshing(store.state.isRefreshing)

            listView {
                size(FILL, FILL)
                dividerHeight(0)
                adapter(adapter.update(store.state.gifs))
                onScroll(fetchMoreGifsAtEndOfListListener)
            }
        }
    }

    val adapter: BansaAdapter<Gif, Gif> = BansaAdapter(
            store.state.gifs,
            { it },
            ::gifView
    )

    val fetchMoreGifsAtEndOfListListener = OnScrolledToEndOfListListener(
            { store.state.isFetching.not() },
            { store.dispatch(FETCH_NEXT_PAGE) })

    var subscription: Subscription? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        store.dispatch(REFRESH)

        subscription = store.subscribe {
            Anvil.render()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription?.unsubscribe()
    }
}
