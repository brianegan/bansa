package com.brianegan.bansa.listOfTrendingDGifs.ui

import android.support.v4.widget.DrawerLayout
import com.brianegan.bansa.listOfTrendingDGifs.store
import com.brianegan.bansa.listOfTrendingGifs.R
import com.brianegan.bansa.listOfTrendingGifs.models.Gif
import com.brianegan.bansa.listOfTrendingGifs.ui.listOfGifsView
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.BansaAdapter
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.OnScrolledToEndOfListListener
import com.brianegan.bansaDevToolsUi.BansaDevToolsPresenter
import trikita.anvil.Anvil
import trikita.anvil.DSL
import trikita.anvil.support.v4.Supportv4DSL

val devToolsPresenter = BansaDevToolsPresenter(store)

internal fun listOfGifsScreen(adapter: BansaAdapter<Gif, Gif>,
                              fetchMoreGifsAtEndOfListListener: OnScrolledToEndOfListListener) {
    Supportv4DSL.drawerLayout {
        listOfGifsView(adapter, fetchMoreGifsAtEndOfListListener)

        DSL.linearLayout {
            DSL.size(DSL.dip(320), DSL.FILL)
            DSL.attr({ v, n, o -> (v.layoutParams as DrawerLayout.LayoutParams).gravity = n }, DSL.START)
            DSL.backgroundResource(R.color.white)

            DSL.xml(R.layout.bansa_dev_tools) {
                DSL.init {
                    devToolsPresenter.unbind() // Clean up any previous instances.
                    devToolsPresenter.bind(Anvil.currentView())
                }
            }
        }
    }
}
