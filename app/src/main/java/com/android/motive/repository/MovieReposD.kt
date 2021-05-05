package com.android.motive.repository

import androidx.lifecycle.LiveData

import com.android.motive.data.source.MovieDetailDataSource
import com.android.motive.data.source.local.entity.MovieKoreaDetails
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable

class MovieReposD(private val apiService: ApiSvc) {

    lateinit var movieDetailDataS: MovieDetailDataSource

    fun getMovieDetailsRepos(composite: CompositeDisposable, id: Int): LiveData<MovieKoreaDetails> {

        movieDetailDataS = MovieDetailDataSource(apiService, composite)
        movieDetailDataS.getDetail(id)

        return movieDetailDataS.movieKoreaResponse

    }

    fun getState(): LiveData<State> = movieDetailDataS.state


}