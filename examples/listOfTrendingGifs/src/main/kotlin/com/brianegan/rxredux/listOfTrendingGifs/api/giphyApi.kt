package com.brianegan.rxredux.listOfTrendingGifs.api

import com.brianegan.rxredux.listOfTrendingGifs.Gif
import com.brianegan.rxredux.listOfTrendingGifs.TrendingGifs
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

data class ApiTrendingGifs(val data: List<ApiGif>, val pagination: NextPage)
data class ApiGif(val images: ApiGifSizes)
data class ApiGifSizes(val original_still: ApiGifLinks)
data class ApiGifLinks(val url: String, val width: Int, val height: Int)
data class NextPage(val count: Int = 25, val offset: Int = 0)

val moshi = Moshi.Builder().build()
val jsonAdapter = moshi.adapter(ApiTrendingGifs::class.javaObjectType)

val toTrendingGifs = Func1<Response, TrendingGifs> {
    val (data, pagination) = jsonAdapter.fromJson(it.body().string())

    TrendingGifs(toGifs(data), pagination)
}

fun toGifs(apiGifs: List<ApiGif>): List<Gif> {
    return apiGifs.map {
        val original = it.images.original_still
        Gif(original.url, original.width, original.height)
    }
}



