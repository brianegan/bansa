package com.brianegan.bansa.listOfTrendingGifs

data class Gif(
        val id: String = "",
        val stillUrl: String = "",
        val videoUrl: String = "",
        val width: Int = 0,
        val height: Int = 0)

data class TrendingGifs(
        val gifs: List<Gif>,
        val pagination: NextPage)

data class ActiveGif(
        val id: String = "",
        val isPlaying: Boolean = false,
        val isFetching: Boolean = false)

data class NextPage(
        val count: Int = 25,
        val offset: Int = 0)
