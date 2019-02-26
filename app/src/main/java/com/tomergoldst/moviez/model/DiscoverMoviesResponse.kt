package com.tomergoldst.moviez.model

import com.google.gson.annotations.SerializedName

data class DiscoverMoviesResponse(

    @SerializedName("page")
    var page: Int,

    @SerializedName("total_results")
    var totalResults: String = "",

    @SerializedName("total_pages")
    var totalPages: String = "",

    @SerializedName("results")
    var results: List<Movie> = ArrayList()


)