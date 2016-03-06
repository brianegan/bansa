package com.brianegan.bansa.listOfTrendingGifs.models

import com.brianegan.bansa.listOfTrendingGifs.api.NextPage

data class TrendingGifs(val gifs: List<Gif>, val pagination: NextPage)
