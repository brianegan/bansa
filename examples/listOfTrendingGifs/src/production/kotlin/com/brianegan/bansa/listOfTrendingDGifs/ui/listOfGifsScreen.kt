package com.brianegan.bansa.listOfTrendingDGifs.ui

import com.brianegan.bansa.listOfTrendingDGifs.store
import com.brianegan.bansa.listOfTrendingGifs.models.Gif
import com.brianegan.bansa.listOfTrendingGifs.ui.listOfGifsView
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.BansaAdapter
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.OnScrolledToEndOfListListener
import com.brianegan.bansaDevToolsUi.BansaDevToolsPresenter

val devToolsPresenter = BansaDevToolsPresenter(store)

internal fun listOfGifsScreen(adapter: BansaAdapter<Gif, Gif>,
                              fetchMoreGifsAtEndOfListListener: OnScrolledToEndOfListListener) {

    listOfGifsView(adapter, fetchMoreGifsAtEndOfListListener)
}
