package com.brianegan.bansa.listOfTrendingGifs

import com.brianegan.bansa.listOfTrendingGifs.api.NextPage

data class Gif(val url: String = "", val width: Int = 0, val height: Int = 0)
data class TrendingGifs(val gifs: List<Gif>, val pagination: NextPage)
