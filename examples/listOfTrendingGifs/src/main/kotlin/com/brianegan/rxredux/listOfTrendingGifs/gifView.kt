package com.brianegan.rxredux.listOfCounters

import android.net.Uri
import android.widget.VideoView
import com.brianegan.rxredux.listOfTrendingGifs.Gif
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

fun gifView(model: Gif) {
    val (videoUrl) = model

    relativeLayout {
        size(FILL, dip(1000))

        videoView {
            val videoView = Anvil.currentView() as VideoView
            size(FILL, WRAP)
            videoView.stopPlayback()
            videoURI(Uri.parse(videoUrl))

            onPrepared {
                it.start()
                it.isLooping = true
            }
        }
    }
}
