package com.brianegan.bansa.listOfTrendingGifs.api

import com.brianegan.bansa.listOfTrendingGifs.Gif
import com.brianegan.bansa.listOfTrendingGifs.TrendingGifs
import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.functions.Func1

fun fetchTrendingGifs(
        offset: Int = 0,
        count: Int = 25)
        : Observable<TrendingGifs> {

    val url = HttpUrl.Builder()
            .scheme("http")
            .host("api.giphy.com")
            .addPathSegment("v1")
            .addPathSegment("gifs")
            .addPathSegment("trending")
            .addQueryParameter("api_key", "dc6zaTOxFJmzC")
            .addQueryParameter("offset", offset.toString())
            .addQueryParameter("count", count.toString())
            .build()

    val request = Request.Builder()
            .get()
            .url(url)
            .build()

    return fetch(request).map(toTrendingGifs)
}

val moshi = Moshi.Builder().build()
val jsonAdapter = moshi.adapter(ApiTrendingGifs::class.javaObjectType)

val toTrendingGifs = Func1<Response, TrendingGifs> {
    val (data, pagination) = jsonAdapter.fromJson(it.body().string())

    TrendingGifs(toGifs(data), pagination)
}

fun toGifs(apiGifs: List<ApiGif>): List<Gif> {
    return apiGifs.map {
        val still = it.images.still
        val original = it.images.original
        Gif(it.id, still.url, original.videoUrl, still.width, still.height)
    }
}



