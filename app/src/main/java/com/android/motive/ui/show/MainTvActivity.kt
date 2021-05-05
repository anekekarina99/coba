@file:Suppress("DEPRECATION")

package com.android.motive.ui.show

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.motive.R
import com.android.motive.data.source.remote.api.ApiConfig
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.repository.ShowRepos
import com.android.motive.ui.adapter.TvAdapter
import com.android.motive.utils.State
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("UNCHECKED_CAST" , "DEPRECATION")
class MainTvActivity : AppCompatActivity() {


    private lateinit var sViewModel : ShowViewModel
    lateinit var mReposShow : ShowRepos
    private lateinit var progress : ProgressBar
    private lateinit var errorT : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tv)

        progress  = findViewById(R.id.progress)
        errorT  = findViewById(R.id.errorT)

        setData()


    }

    private fun setData() {
        setApi()
        setViewModel()
        setAdapter()
    }

    private fun setAdapter() {
        val tAdapter = TvAdapter(this)
        val linearLayoutManager = GridLayoutManager(this, 2)

        linearLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup()
        {
            override fun getSpanSize(position : Int) : Int
            {
                val viewType = tAdapter.getItemViewType(position)
                return if (viewType == 1) 1
                else 2
            }

        }
        rvMovie.layoutManager = linearLayoutManager
        rvMovie.setHasFixedSize(true)
        rvMovie.adapter = tAdapter

        sViewModel.tPList.observe(this, Observer {
            tAdapter.submitList(it)
        })
        sViewModel.stateNet.observe(this, Observer { s->

            if(sViewModel.listKosong() && s == State.LOADING ){
                progress.visibility = View.VISIBLE
            }else{
                progress.visibility = View.GONE
            }

            if(sViewModel.listKosong() && s == State.ERROR ){
                errorT.visibility = View.VISIBLE
            }else{
                errorT.visibility = View.GONE
            }

            if(!sViewModel.listKosong()){
                tAdapter.setNetTv(s)
            }

        })

    }

    private fun setViewModel() {
        sViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
                return ShowViewModel(mReposShow) as T
            }
        })[ShowViewModel :: class.java]
    }

    private fun setApi() {
        val apiS : ApiSvc =  ApiConfig.getClient()
        mReposShow = ShowRepos(apiS)
    }
}
