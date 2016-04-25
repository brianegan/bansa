package com.brianegan.bansa.listOfTrendingGifs.state

import android.content.res.Configuration
import com.brianegan.bansa.listOfTrendingGifs.api.NextPage
import com.brianegan.bansa.listOfTrendingGifs.models.Gif

data class ApplicationState(val isRefreshing: Boolean = true,
                            val isFetching: Boolean = false,
                            val gifs: List<Gif> = arrayListOf(),
                            val pagination: NextPage = NextPage(),
                            val orientation: Int = Configuration.ORIENTATION_UNDEFINED)
