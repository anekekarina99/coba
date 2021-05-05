package com.android.motive.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.motive.data.source.local.entity.MovieKorea
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieKoreaDataSource(
    private val api :ApiSvc ,
    private val compositeDisposable :CompositeDisposable
) : PageKeyedDataSource<Int ,MovieKorea>() {

    private var page = 1

    val state :MutableLiveData<State> = MutableLiveData()

    companion object {
        const val TAG = "MovieKoreaDataSource"
    }

    override fun loadInitial(
        params :LoadInitialParams<Int> ,
        callback :LoadInitialCallback<Int ,MovieKorea>
    ) {
        state.postValue(State.LOADING)
        compositeDisposable.add(
            api.getMoviePopular(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results ,null ,page + 1)
                        state.postValue(State.LOADED)
                        Log.d(this@MovieKoreaDataSource.toString() ,"Berhasil getMovieKorea")

                    } ,
                    {
                        state.postValue(State.ERROR)
                        it.message?.let { it1 -> Log.e(TAG ,it1) }
                    }
                )
        )

    }

    override fun loadBefore(params :LoadParams<Int> ,callback :LoadCallback<Int ,MovieKorea>) {

    }

    override fun loadAfter(params :LoadParams<Int> ,callback :LoadCallback<Int ,MovieKorea>) {
        state.postValue(State.LOADING)

        compositeDisposable.add(
            api.getMoviePopular(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages < params.key) {
                            state.postValue(State.END)
                        } else {
                            callback.onResult(it.results ,params.key + 1)
                            state.postValue(State.LOADED)
                        }
                    } ,{
                        state.postValue(State.ERROR)
                        it.message?.let { it1 -> Log.e(TAG ,it1) }
                    }

                )
        )
    }


}