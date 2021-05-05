package com.android.motive.data.source.remote.api

import com.android.motive.data.source.local.entity.MovieKoreaDetails
import com.android.motive.data.source.local.entity.TvKoreaDetail
import com.android.motive.data.source.local.response.Response
import com.android.motive.data.source.local.response.ResponseTv
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSvc {

    @GET("movie/popular")
    fun getMoviePopular(@Query("page") page : Int) : Single<Response>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieKoreaDetails>

    @GET("tv/{tv_id}")
    fun getTvDetails(@Path("tv_id") id : Int ) : Single<TvKoreaDetail>

    @GET("tv/top_list")
    fun getTvList(@Query("page") page : Int) : Single<ResponseTv>


}