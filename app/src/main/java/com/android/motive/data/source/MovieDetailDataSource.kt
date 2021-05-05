package com.android.motive.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.data.source.local.entity.MovieKoreaDetails
import com.android.motive.data.source.local.entity.TvKoreaDetail
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailDataSource(private val apiS :ApiSvc ,private val compositeDis :CompositeDisposable) {

    private val _statusInt = MutableLiveData<State>()
    val state :LiveData<State>
        get() = _statusInt

    private val _movieDetailRes = MutableLiveData<MovieKoreaDetails>()
    val movieKoreaResponse :LiveData<MovieKoreaDetails>
        get() = _movieDetailRes

    private val _tvDetailRes = MutableLiveData<TvKoreaDetail>()
    val tvKoreaResponse :LiveData<TvKoreaDetail>
        get() = _tvDetailRes

    companion object {
        const val TAG = "MovieDetailDataSource"
    }

    fun getTvDetail(id :Int) {
        _statusInt.postValue(State.LOADING)

        try {
            compositeDis.add(
                apiS.getTvDetails(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { m ->
                            _tvDetailRes.postValue(m)
                            _statusInt.postValue(State.LOADED)
                            Log.d(this@MovieDetailDataSource.toString() ,"Berhasil getKoreaDetail")
                        } ,
                        {
                            _statusInt.postValue(State.ERROR)
                            it.message?.let { it1 -> Log.e(TAG ,it1) }
                        }
                    )
            )

        } catch (e :Exception) {
            e.message?.let { Log.e(TAG ,it) }
        }


    }




    fun getDetail(id :Int) {

        _statusInt.postValue(State.LOADING)


        try {
            compositeDis.add(
                apiS.getMovieDetails(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { m ->
                            _movieDetailRes.postValue(m)
                            _statusInt.postValue(State.LOADED)
                            Log.d(this@MovieDetailDataSource.toString() ,"Berhasil getDetail")
                        } ,
                        {
                            _statusInt.postValue(State.ERROR)
                            it.message?.let { it1 -> Log.e(TAG ,it1) }
                        }
                    )
            )

        } catch (e :Exception) {
            e.message?.let { Log.e(TAG ,it) }
        }


    }
}