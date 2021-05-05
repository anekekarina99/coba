@file:Suppress("DEPRECATION")

package com.android.motive.ui.show


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.android.motive.ui.adapter.MovieAdapter
import com.android.motive.utils.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_net.*

@Suppress("UNCHECKED_CAST" , "DEPRECATION")
class MainActivity : AppCompatActivity() {


    private lateinit var sViewModel : ShowViewModel
    lateinit var mReposShow : ShowRepos
    private lateinit var progress : ProgressBar
    private lateinit var errorT : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val mAdapter = MovieAdapter(this)
        val linearLayoutManager = GridLayoutManager(this, 2)

        linearLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = mAdapter.getItemViewType(position)
                return if (viewType == 1) 1
                else 2
            }
        }

        rvMovie.layoutManager = linearLayoutManager
        rvMovie.setHasFixedSize(true)
        rvMovie.adapter = mAdapter


        sViewModel.mPList.observe(this, Observer {
            mAdapter.submitList(it)
        })
        sViewModel.stateNet.observe(this , Observer { s ->


            if (sViewModel.listKosong() && s == State.LOADING)
            {
               setProgressBar(true)
            }
            else
            {
                setProgressBar(false)
            }

            if (sViewModel.listKosong() && s == State.ERROR)
            {
                errorT.visibility = View.VISIBLE
            }
            else
            {
                errorT.visibility = View.GONE
            }

            if (!sViewModel.listKosong())
            {
                mAdapter.setNet(s)
            }

        })


    }

  private fun setProgressBar(b : Boolean) = if(b){
      progress.visibility = View.VISIBLE
  }else{
      progress.visibility = View.GONE
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
