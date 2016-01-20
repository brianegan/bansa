package com.brianegan.rxredux.listOfCounters

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.Surface
import android.view.TextureView
import com.brianegan.rxredux.listOfTrendingGifs.Gif
import com.brianegan.rxredux.listOfTrendingGifs.view.attrs.embedVideo

fun gifView(model: Gif) {
    val (videoUrl) = model

    println(model)

    textureView {
        size(FILL, dip(300))
        margin(0, 0, 0, dip(20))
        embedVideo(videoUrl)
    }
}

fun getListener(url : String): TextureView.SurfaceTextureListener {
    return object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            println("Surface Texture Destroyed")
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            val s = Surface(surface);
            val mediaPlayer = MediaPlayer()

            mediaPlayer.setDataSource(url)
            mediaPlayer.setSurface(s)
            mediaPlayer.prepare()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }
}
