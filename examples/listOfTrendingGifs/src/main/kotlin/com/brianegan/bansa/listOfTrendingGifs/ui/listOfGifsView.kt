package com.brianegan.bansa.listOfTrendingGifs.ui

import com.brianegan.bansa.listOfTrendingDGifs.store
import com.brianegan.bansa.listOfTrendingGifs.R
import com.brianegan.bansa.listOfTrendingGifs.actions.REFRESH
import com.brianegan.bansa.listOfTrendingGifs.models.Gif
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.BansaAdapter
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.OnScrolledToEndOfListListener
import trikita.anvil.DSL
import trikita.anvil.support.v4.Supportv4DSL.*

internal fun listOfGifsView(adapter: BansaAdapter<Gif, Gif>, fetchMoreGifsAtEndOfListListener: OnScrolledToEndOfListListener) {
    swipeRefreshLayout {
        size(DSL.FILL)
        DSL.id(R.id.content)
        onRefresh { store.dispatch(REFRESH) }
        refreshing(store.state.isRefreshing)

        DSL.listView {
            DSL.size(DSL.FILL, DSL.FILL)
            DSL.dividerHeight(0)
            DSL.adapter(adapter.update(store.state.gifs))
            DSL.onScroll(fetchMoreGifsAtEndOfListListener)
        }
    }
}
