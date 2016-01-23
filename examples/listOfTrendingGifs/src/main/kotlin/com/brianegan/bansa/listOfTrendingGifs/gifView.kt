package com.brianegan.bansa.listOfCounters

import android.content.Context
import android.graphics.Point
import android.net.Uri.parse
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.brianegan.bansa.Action
import com.brianegan.bansa.Store
import com.brianegan.bansa.listOfTrendingGifs.ApplicationState
import com.brianegan.bansa.listOfTrendingGifs.Gif
import com.brianegan.bansa.listOfTrendingGifs.PLAY_GIF
import com.brianegan.bansa.listOfTrendingGifs.STOP_GIF
import com.brianegan.bansa.listOfTrendingGifs.ui.src
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

fun shouldShow(): Boolean {
    return Math.random() > 0.9
}

fun gifView(model: GifViewModel) {
    val (gif, isPlaying, isFetching, onTouchStart, onTouchEnd) = model
    val (id, stillUrl, videoUrl, width, height) = gif
    val deviceMetrics = measureDevice(Anvil.currentView<View>().context)
    val widthRatio = deviceMetrics.x.toFloat().div(width)

    frameLayout {
        val currentView = Anvil.currentView<FrameLayout>()
        currentView.setTag(com.brianegan.bansa.listOfTrendingGifs.R.id.touchStart, onTouchStart)
        currentView.setTag(com.brianegan.bansa.listOfTrendingGifs.R.id.touchEnd, onTouchEnd)

        init {
            onTouch(View.OnTouchListener { view, motionEvent ->
                val onTouchStart = currentView.getTag(com.brianegan.bansa.listOfTrendingGifs.R.id.touchStart) as () -> Action
                val onTouchEnd = currentView.getTag(com.brianegan.bansa.listOfTrendingGifs.R.id.touchEnd) as () -> Action

                when (motionEvent.action) {
                    ACTION_DOWN -> {
                        onTouchStart()
                        true
                    }
                    ACTION_CANCEL -> {
                        onTouchEnd()
                        false
                    }
                    else -> false
                }
            })
        }

        size(deviceMetrics.x, height.toFloat().times(widthRatio).toInt())

        imageView {
            size(FILL, FILL)
            src(stillUrl)
        }

        if (isFetching.or(isPlaying)) {
            textureView {
                size(FILL, dip(300))
                margin(0, 0, 0, dip(20))
                embedVideo(videoUrl)
            }
        }

//        if (isFetching) {
//            textView {
//                size(FILL, FILL)
//                backgroundResource(com.brianegan.bansa.listOfTrendingGifs.R.color.colorPrimaryDark)
//                textColor(com.brianegan.bansa.listOfTrendingGifs.R.color.colorAccent)
//                text("Fetching")
//            }
//        }
    }
}

data class GifViewModel(
        val gif: Gif,
        val isPlaying: Boolean,
        val isFetching: Boolean,
        val onTouchStart: () -> Action,
        val onTouchEnd: () -> Action)

val buildMapGifToGifViewModel = { store: Store<ApplicationState, Action> ->
    { gif: Gif ->
        val isActive = gif.id.equals(store.getState().activeGif.id)
        val isPlaying = isActive.and(store.getState().activeGif.isPlaying)
        val isFetching = isActive.and(store.getState().activeGif.isFetching)
        val onTouchStart = { store.dispatch(PLAY_GIF(gif.id)) }
        val onTouchEnd = { store.dispatch(STOP_GIF(gif.id)) }

        GifViewModel(gif, isPlaying, isFetching, onTouchStart, onTouchEnd)
    }
}

private fun measureDevice(context: Context): Point {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
}
