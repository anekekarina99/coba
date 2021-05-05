package com.android.motive.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.android.motive.data.source.MovieKoreaDataSource
import com.android.motive.data.source.local.entity.MovieKorea
import com.android.motive.data.source.remote.api.ApiSvc
import io.reactivex.disposables.CompositeDisposable

class MovieFactory(private val api :ApiSvc ,private val composite :CompositeDisposable) :
    DataSource.Factory<Int ,MovieKorea>() {

    val mDataSourceLive = MutableLiveData<MovieKoreaDataSource>()

    override fun create() :DataSource<Int ,MovieKorea> {
        val mDataSource :MovieKoreaDataSource = MovieKoreaDataSource(api ,composite)
        mDataSourceLive.postValue(mDataSource)
        return mDataSource
    }

}