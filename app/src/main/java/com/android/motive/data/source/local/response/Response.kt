package com.android.motive.data.source.local.response

import com.android.motive.data.source.local.entity.MovieKorea
import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("page")
    val page :Int ,
    @SerializedName("results")
    val results :List<MovieKorea> ,
    @SerializedName("total_pages")
    val totalPages :Int ,
    @SerializedName("total_results")
    val totalResults :Int
)