package com.android.motive.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.motive.data.source.local.entity.TvKorea
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TvKoreaDataSource(
    private val api : ApiSvc ,
    private val composite : CompositeDisposable
) : PageKeyedDataSource<Int , TvKorea>() {

    private var page = 1

    val state : MutableLiveData<State> = MutableLiveData()

    companion object {
        const val TAG = "TvKoreaDataSource"
    }

    override fun loadInitial(
        params : LoadInitialParams<Int> ,
        callback : LoadInitialCallback<Int , TvKorea>
    ) {
        state.postValue(State.LOADING)
        composite.add(
            api.getTvList(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results ,null ,page + 1)
                        state.postValue(State.LOADED)
                        Log.d(this@TvKoreaDataSource.toString() ,"Berhasil getTvKorea")

                    } ,
                    {
                        state.postValue(State.ERROR)
                        it.message?.let { it1 -> Log.e(TAG ,it1) }
                    }
                )
        )

    }

    override fun loadBefore(params : LoadParams<Int> , callback : LoadCallback<Int , TvKorea>) {

    }

    override fun loadAfter(params : LoadParams<Int> , callback : LoadCallback<Int , TvKorea>) {
        state.postValue(State.LOADING)

        composite.add(
            api.getTvList(params.key)
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