package com.brianegan.rxredux.randomGif

import android.animation.ObjectAnimator
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.VideoView
import com.brianegan.RxRedux.Action
import com.brianegan.RxRedux.Store
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.subscriptions.Subscriptions
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

public class RootView(c: Context, val store: Store<ApplicationState, Action>) : RenderableView(c) {
    override fun view() {
        template(buildPresentationModel())
    }

    val fetchRandomGif = View.OnClickListener {
        store.dispatch(FETCH_RANDOM_GIF)
    }

    private fun buildPresentationModel(): ViewModel {
        val (isFetching, videoUrl) = store.getState()

        return ViewModel(videoUrl, isFetching, fetchRandomGif)
    }

    private fun template(model: ViewModel) {
        val (videoUrl, isFetching, fetchRandomGif) = model

        linearLayout {
            size(FILL, WRAP)
            orientation(LinearLayout.VERTICAL)

            button {
                size(FILL, WRAP)
                padding(dip(10))
                text("Moar Please!")
                onClick(fetchRandomGif)
            }

            frameLayout {
                size(FILL, FILL)
                gravity(CENTER)

                videoView {
                    val currentView = Anvil.currentView() as VideoView
                    size(FILL, FILL)
                    videoURI(Uri.parse(videoUrl))

                    if (isFetching) {
                        visibility(GONE)
                        currentView.stopPlayback()
                    } else {
                        visibility(VISIBLE)
                    }

                    onPrepared {
                        it.start()
                        it.isLooping = true
                    }
                }

                textView() {
                    size(FILL, FILL)
                    gravity(CENTER)
                    text("Fetching")
                    fadeInWhen(isFetching, Anvil.currentView())
                    fadeOutWhen(!isFetching, Anvil.currentView())
                }
            }
        }
    }

    private fun fadeInWhen(shouldFadeIn: Boolean, view: View) {
        anim(shouldFadeIn, ObjectAnimator.ofFloat(view, "alpha", 0f, 1f))
    }

    private fun fadeOutWhen(shouldFadeOut: Boolean, view: View) {
        anim(shouldFadeOut, ObjectAnimator.ofFloat(view, "alpha", 1f, 0f))
    }

    var subscription: Subscription = Subscriptions.empty()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        subscription = store.state.observeOn(AndroidSchedulers.mainThread()).subscribe(Action1 {
            Anvil.render()
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscription.unsubscribe()
    }

    data class ViewModel(val videoUrl: String, val isFetching: Boolean, val fetchRandomGif: View.OnClickListener)
}
