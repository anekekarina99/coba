package com.android.motive.data.source.local.response

import com.android.motive.data.source.local.entity.TvKorea
import com.google.gson.annotations.SerializedName

data class ResponseTv (
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TvKorea>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)