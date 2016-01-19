package com.brianegan.rxredux.randomGif

import com.squareup.moshi.Json

import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.functions.Func1

fun randomGif(tag: String = ""): Observable<String> {
    val url = HttpUrl.Builder()
            .scheme("http")
            .host("api.giphy.com")
            .addPathSegment("v1")
            .addPathSegment("gifs")
            .addPathSegment("random")
            .addQueryParameter("api_key", "dc6zaTOxFJmzC")
            .addQueryParameter("tag", tag)
            .build()

    val request = Request.Builder()
            .get()
            .url(url)
            .build()

    return fetch(request).map(toRandomGifUrl)
}

data class RandomGifWrapper(val data: RandomGif)
data class RandomGif(@Json(name = "image_mp4_url") val videoUrl: String)

val toRandomGifUrl = Func1<Response, String> {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(RandomGifWrapper::class.javaObjectType)

    jsonAdapter.fromJson(it.body().string()).data.videoUrl
}



