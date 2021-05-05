package com.android.motive.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.android.motive.data.source.MovieKoreaDataSource
import com.android.motive.data.source.TvKoreaDataSource
import com.android.motive.data.source.local.entity.MovieKorea
import com.android.motive.data.source.local.entity.TvKorea
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.utils.State
import com.android.motive.viewmodels.MovieFactory
import com.android.motive.viewmodels.TvFactory
import io.reactivex.disposables.CompositeDisposable

class ShowRepos(private val api :ApiSvc) {

    private lateinit var mDFactory :MovieFactory
    private lateinit var mPList :LiveData<PagedList<MovieKorea>>

    private lateinit var tDFactory : TvFactory
    private lateinit var tPList :LiveData<PagedList<TvKorea>>

    fun getMovie(composite :CompositeDisposable) :LiveData<PagedList<MovieKorea>> {
        mDFactory = MovieFactory(api ,composite)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()

        mPList = LivePagedListBuilder(mDFactory ,config).build()

        return mPList
    }

    fun getTv(composite :CompositeDisposable) :LiveData<PagedList<TvKorea>> {
        tDFactory = TvFactory(api ,composite)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()

        tPList = LivePagedListBuilder(tDFactory ,config).build()

        return tPList
    }

    fun getTvState() :LiveData<State> {
        return Transformations.switchMap<TvKoreaDataSource ,State>(
            this.tDFactory.tDataSourceLive , TvKoreaDataSource::state
        )
    }

    fun getState() :LiveData<State> {
        return Transformations.switchMap<MovieKoreaDataSource ,State>(
            this.mDFactory.mDataSourceLive ,MovieKoreaDataSource::state
        )
    }
}