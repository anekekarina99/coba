package com.android.motive.ui.detailTv

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.motive.R
import com.android.motive.data.source.remote.api.ApiConfig
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.repository.TvReposD
import com.android.motive.ui.show.MainActivity
import com.android.motive.utils.Constant
import com.android.motive.utils.State
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

@Suppress("DEPRECATION")
class DetailTvActivity : AppCompatActivity() {

    private lateinit var viewModel :DetailTvViewModel
    private lateinit var tvRepos :TvReposD
    private var id :Int = 1


    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv)
        setData()
        backBtn()

    }

    private fun setData() {
        getApi()

        setViewModel()

        getTvKoreaDetail()

        getNetworkState()
    }

    private fun getApi() {
        id = intent.getIntExtra("id" ,1)
        val api :ApiSvc = ApiConfig.getClient()
        tvRepos = TvReposD(api)
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this ,object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass :Class<T>) :T {
                @Suppress("UNCHECKED_CAST")
                return DetailTvViewModel(tvRepos ,id) as T
            }
        })[DetailTvViewModel::class.java]
    }

    private fun getNetworkState() {

        viewModel.networkState.observe(this ,Observer {
            progressBarr.visibility = if (it == State.LOADING) View.VISIBLE else View.GONE
            txtError.visibility = if (it == State.ERROR) View.VISIBLE else View.GONE


        })
    }

    private fun getTvKoreaDetail() {
        viewModel.tvKoreaDetails.observe(this ,Observer {
            tvTitle.text = it.originalName
            tvDes.text = it.overview
            tvProd.text = it.inProduction.toString()
            tvChar.text = it.tagline

            Glide.with(this)
                .load(Constant.POSTER_BASE_URL + it.posterPath)
                .into(ivDetail)

        })
    }

    private fun backBtn() {
        btnBackDet.setOnClickListener {
            val i = Intent(this ,MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

}