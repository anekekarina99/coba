package com.android.motive.ui.detailTv

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.motive.data.source.local.entity.MovieKoreaDetails
import com.android.motive.data.source.local.entity.TvKoreaDetail
import com.android.motive.repository.MovieReposD
import com.android.motive.repository.TvReposD
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable

class DetailTvViewModel(private val tvRes :TvReposD ,id: Int)  : ViewModel() {

    private val composite = CompositeDisposable()

    val tvKoreaDetails :LiveData<TvKoreaDetail> by lazy {
        tvRes.getTvDetailsRepos(composite ,id)
    }

    val networkState :LiveData<State> by lazy {
        tvRes.getState()
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }



}