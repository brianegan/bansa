package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.rxredux.listOfTrendingGifs.api.NextPage

data class Gif(val url: String = "", val width: Int = 0, val height: Int = 0)
data class TrendingGifs(val gifs: List<Gif>, val pagination: NextPage)
