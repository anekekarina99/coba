package com.android.motive.repository

import androidx.lifecycle.LiveData
import com.android.motive.data.source.MovieDetailDataSource
import com.android.motive.data.source.local.entity.TvKoreaDetail
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable

class TvReposD(private val apiService :ApiSvc) {

    lateinit var movieDetailDataS :MovieDetailDataSource

    fun getTvDetailsRepos(composite :CompositeDisposable ,id :Int) :LiveData<TvKoreaDetail> {

        movieDetailDataS = MovieDetailDataSource(apiService ,composite)
        movieDetailDataS.getDetail(id)

        return movieDetailDataS.tvKoreaResponse

    }

    fun getState() :LiveData<State> = movieDetailDataS.state


}