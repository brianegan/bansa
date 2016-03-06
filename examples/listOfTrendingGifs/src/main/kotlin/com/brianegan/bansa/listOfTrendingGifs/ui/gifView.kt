package com.brianegan.bansa.listOfTrendingGifs.ui

import com.brianegan.bansa.listOfTrendingGifs.models.Gif
import com.brianegan.bansa.listOfTrendingGifs.ui.utils.src
import trikita.anvil.DSL.imageView
import trikita.anvil.DSL.size

fun gifView(model: Gif) {
    val (url, width, height) = model
    val deviceMetrics = com.brianegan.bansa.listOfTrendingGifs.ui.measureDevice(trikita.anvil.Anvil.currentView<android.view.View>().context)
    val widthRatio = deviceMetrics.x.toFloat().div(width)

    imageView {
        size(deviceMetrics.x, height.toFloat().times(widthRatio).toInt())
        src(url)
    }
}

private fun measureDevice(context: android.content.Context): android.graphics.Point {
    val wm = context.getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager
    val display = wm.defaultDisplay
    val point = android.graphics.Point()
    display.getSize(point)
    return point
}
