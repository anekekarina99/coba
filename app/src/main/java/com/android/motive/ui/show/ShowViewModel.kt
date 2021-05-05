package com.android.motive.ui.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.android.motive.data.source.local.entity.MovieKorea
import com.android.motive.data.source.local.entity.TvKorea
import com.android.motive.repository.ShowRepos
import com.android.motive.utils.State
import io.reactivex.disposables.CompositeDisposable

class ShowViewModel(private val mRepos : ShowRepos ) : ViewModel(){
    private val composite = CompositeDisposable()

    val mPList : LiveData<PagedList<MovieKorea>> by lazy {
        mRepos.getMovie(composite)
    }

    val tPList : LiveData<PagedList<TvKorea>> by lazy {
        mRepos.getTv(composite)
    }

    val stateNet : LiveData<State> by lazy {
        mRepos.getState()
    }

    fun listKosong() : Boolean {
        return mPList.value?.isEmpty() ?:true
    }

    fun listTvKosong() : Boolean {
        return tPList.value?.isEmpty() ?:true
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }


}