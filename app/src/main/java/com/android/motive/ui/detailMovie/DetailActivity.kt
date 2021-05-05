@file:Suppress("DEPRECATION")

package com.android.motive.ui.detailMovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.android.motive.R
import com.android.motive.data.source.remote.api.ApiConfig
import com.android.motive.data.source.remote.api.ApiSvc
import com.android.motive.repository.MovieReposD
import com.android.motive.ui.show.MainActivity
import com.android.motive.utils.Constant.Companion.POSTER_BASE_URL
import com.android.motive.utils.State
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {


    private lateinit var viewModel :DetailViewModel
    private lateinit var movieRepos :MovieReposD
    private var id :Int = 1


    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setData()
        backBtn()

    }

    private fun setData() {
        getApi()

        setViewModel()

        getMovieKoreaDetail()

        getNetworkState()
    }

    private fun getApi() {
        id = intent.getIntExtra("id" ,1)
        val api :ApiSvc = ApiConfig.getClient()
        movieRepos = MovieReposD(api)
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this ,object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass :Class<T>) :T {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(movieRepos ,id) as T
            }
        })[DetailViewModel::class.java]
    }

    private fun getNetworkState() {

        viewModel.networkState.observe(this ,Observer {
            progressBarr.visibility = if (it == State.LOADING) View.VISIBLE else View.GONE
            txtError.visibility = if (it == State.ERROR) View.VISIBLE else View.GONE


        })
    }

    private fun getMovieKoreaDetail() {
        viewModel.movieKoreaDetails.observe(this ,Observer {
            tvTitle.text = it.title
            tvDes.text = it.overview
            tvProd.text = it.releaseDate
            tvChar.text = it.tagline

            Glide.with(this)
                .load(POSTER_BASE_URL + it.posterPath)
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
