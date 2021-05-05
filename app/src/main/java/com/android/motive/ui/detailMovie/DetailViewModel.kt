package com.android.motive.ui.detailMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.motive.utils.State
import com.android.motive.data.source.local.entity.MovieKoreaDetails
import com.android.motive.repository.MovieReposD
import io.reactivex.disposables.CompositeDisposable

class DetailViewModel (private val movieRes : MovieReposD, id: Int)  : ViewModel() {

    private val composite = CompositeDisposable()

    val movieKoreaDetails :LiveData<MovieKoreaDetails> by lazy {
        movieRes.getMovieDetailsRepos(composite ,id)
    }

    val networkState : LiveData<State> by lazy {
        movieRes.getState()
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }



}