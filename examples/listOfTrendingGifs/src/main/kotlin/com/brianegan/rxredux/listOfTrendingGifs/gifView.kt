package com.brianegan.rxredux.listOfCounters

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import com.brianegan.rxredux.listOfTrendingGifs.Gif
import com.brianegan.rxredux.listOfTrendingGifs.LoadImage.loadImage
import com.brianegan.rxredux.listOfTrendingGifs.R
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

fun gifView(model: Gif) {
    val (videoUrl, width, height) = model
    val deviceMetrics = measureDevice(Anvil.currentView<View>().context)
    val widthRatio = deviceMetrics.x.toFloat().div(width)

    relativeLayout {
        size(deviceMetrics.x, height.toFloat().times(widthRatio).toInt())
        margin(0, 0, 0, dip(20))

        imageView {
            size(dip(500), dip(500))
            loadImage(videoUrl)
            backgroundColor(R.color.abc_primary_text_material_dark)
        }
    }
}

private fun measureDevice(context: Context): Point {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
}
