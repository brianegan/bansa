package com.brianegan.rxredux.listOfTrendingGifs.ui

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL.attr

private val imageSrcFunc = ImageSrcFunc()

fun src(url: String): Unit {
    attr<String>(imageSrcFunc, url)
}

private class ImageSrcFunc : Anvil.AttrFunc<String> {
    override fun apply(view: View, newUrl: String, oldUrl: String?) {
        if (view is ImageView && newUrl != oldUrl) {
            Picasso
                    .with(view.getContext())
                    .load(newUrl)
                    .into(view)
        }
    }
}
