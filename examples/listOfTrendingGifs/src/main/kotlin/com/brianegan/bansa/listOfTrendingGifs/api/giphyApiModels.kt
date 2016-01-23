package com.brianegan.bansa.listOfTrendingGifs.api

import com.brianegan.bansa.listOfTrendingGifs.NextPage
import com.squareup.moshi.Json

data class ApiTrendingGifs(
        val data: List<ApiGif>,
        val pagination: NextPage)

data class ApiGif(
        val id: String,
        val images: ApiGifFormats)

data class ApiGifFormats(
        val original: ApiGifOriginalFormat,
        @Json(name = "original_still") val still: ApiGifStillFormat)

data class ApiGifStillFormat(
        val id: String,
        val url: String,
        val width: Int,
        val height: Int)

data class ApiGifOriginalFormat(
        @Json(name = "mp4") val videoUrl: String)
