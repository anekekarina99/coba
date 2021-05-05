package com.android.motive.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.android.motive.data.source.MovieKoreaDataSource
import com.android.motive.data.source.TvKoreaDataSource
import com.android.motive.data.source.local.entity.MovieKorea
import com.android.motive.data.source.local.entity.TvKorea
import com.android.motive.data.source.remote.api.ApiSvc
import io.reactivex.disposables.CompositeDisposable

class TvFactory(private val api : ApiSvc , private val composite : CompositeDisposable) :
    DataSource.Factory<Int , TvKorea>() {

    val tDataSourceLive = MutableLiveData<TvKoreaDataSource>()

    override fun create() : DataSource<Int , TvKorea> {
        val tDataSource : TvKoreaDataSource =TvKoreaDataSource(api ,composite)
        tDataSourceLive.postValue(tDataSource)
        return tDataSource
    }

}