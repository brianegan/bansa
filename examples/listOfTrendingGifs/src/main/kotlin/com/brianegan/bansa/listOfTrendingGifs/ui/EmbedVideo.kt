package com.brianegan.bansa.listOfTrendingGifs.ui

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.Surface
import android.view.TextureView
import android.view.View

import com.brianegan.bansa.listOfTrendingGifs.R
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL.attr
import trikita.anvil.DSL.surfaceTextureListener

private class EmbedVideoFunc : Anvil.AttrFunc<String> {
    override fun apply(v: View, newVideo: String, oldVideo: String?) {
        if (v is TextureView) {
            val tag = v.getTag(R.id.video_controller)

            if (oldVideo == null) {
                val embedVideoController = EmbedVideoController(v, newVideo)
                surfaceTextureListener(EmbedVideoController(v, newVideo))
                v.setTag(R.id.video_controller, embedVideoController)
            } else if (newVideo != oldVideo) {
                val previousController = v.getTag(R.id.video_controller) as EmbedVideoController
                previousController.updateDataSource(newVideo)
            }
        }
    }
}

private val embedVideoFuncInstance = EmbedVideoFunc()

fun embedVideo(videoUrl: String) {
    attr(embedVideoFuncInstance, videoUrl)
}

class EmbedVideoController(private val view: View, var url: String) : TextureView.SurfaceTextureListener {
    val mediaPlayer = MediaPlayer()

    fun start() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun updateDataSource(url: String) {
        stop()
        this.url = url
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        val s = Surface(surface);

        mediaPlayer.setDataSource(url)
        mediaPlayer.setSurface(s)
        mediaPlayer.prepare()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }
}
