package com.brianegan.bansa.listOfCounters

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import com.brianegan.bansa.listOfTrendingGifs.Gif
import com.brianegan.bansa.listOfTrendingGifs.ui.src
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

fun gifView(model: Gif) {
    val (url, width, height) = model
    val deviceMetrics = measureDevice(Anvil.currentView<View>().context)
    val widthRatio = deviceMetrics.x.toFloat().div(width)

    imageView {
        size(deviceMetrics.x, height.toFloat().times(widthRatio).toInt())
        margin(0, 0, 0, dip(20))
        src(url)
    }
}

private fun measureDevice(context: Context): Point {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
}
