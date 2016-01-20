package com.brianegan.rxredux.listOfTrendingGifs

import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.functions.Func1

fun fetchTrendingGifs(): Observable<TrendingGifs> {
    val url = HttpUrl.Builder()
            .scheme("http")
            .host("api.giphy.com")
            .addPathSegment("v1")
            .addPathSegment("gifs")
            .addPathSegment("trending")
            .addQueryParameter("api_key", "dc6zaTOxFJmzC")
            .build()

    val request = Request.Builder()
            .get()
            .url(url)
            .build()

    return fetch(request).map(toTrendingGifs)
}

data class TrendingGifs(val gifs: List<Gif>, val pagination: NextPage)
data class ApiTrendingGifs(val data: List<ApiGif>, val pagination: NextPage)
data class ApiGif(val images: ApiGifSizes)
data class ApiGifSizes(val original: ApiGifLinks)
data class ApiGifLinks(val mp4: String, val width: Int, val height: Int)
data class NextPage(val count: Int, val offset: Int)

val toTrendingGifs = Func1<Response, TrendingGifs> {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(ApiTrendingGifs::class.javaObjectType)
    val (data, pagination) = jsonAdapter.fromJson(it.body().string())

    TrendingGifs(toGifs(data), pagination)
}

fun toGifs(apiGifs: List<ApiGif>): List<Gif> {
    return apiGifs.map {
        val original = it.images.original
        Gif(original.mp4, original.width, original.height)
    }
}



