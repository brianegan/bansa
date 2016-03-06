package com.brianegan.bansa.listOfTrendingGifs.ui.utils

import android.widget.AbsListView

class OnScrolledToEndOfListListener(val shouldFireCallback: () -> Boolean,
                                    val callback: () -> Unit,
                                    val numItemsAwayFromEndThreshold: Int = 5) : AbsListView.OnScrollListener {

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        val lastVisibleItem = firstVisibleItem.plus(visibleItemCount);
        if (lastVisibleItem == totalItemCount.minus(numItemsAwayFromEndThreshold) && shouldFireCallback()) {
            callback();
        }
    }
}
