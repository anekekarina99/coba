package com.android.motive.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.motive.R
import com.android.motive.data.source.local.entity.MovieKorea
import com.android.motive.ui.detailMovie.DetailActivity
import com.android.motive.utils.Constant.Companion.MOVIE_VIEW_TYPE
import com.android.motive.utils.Constant.Companion.NETWORK_VIEW_TYPE
import com.android.motive.utils.Constant.Companion.POSTER_BASE_URL
import com.android.motive.utils.State
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_net.view.*

class MovieAdapter(public val context: Context) : PagedListAdapter<MovieKorea, RecyclerView.ViewHolder>(MCallback()) {


    private var state : State? = null


    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : RecyclerView.ViewHolder {

        return if (viewType == NETWORK_VIEW_TYPE) {
            val myView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_list , parent , false)
            NetworkViewHolder(myView)
        } else{
            val myView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_net , parent , false)
            MovieViewHolder(myView)
        }

    }

    override fun onBindViewHolder(holder :RecyclerView.ViewHolder , position :Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE) {
            getItem(position)?.let { (holder as MovieViewHolder).binding(it , context) }
        }else{
            state?.let { (holder as NetworkViewHolder).binding(it) }
        }

    }



    class MCallback : DiffUtil.ItemCallback<MovieKorea>() {
        override fun areItemsTheSame(oldItem: MovieKorea, newItem: MovieKorea): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieKorea, newItem: MovieKorea): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position : Int) : Int
    {
        return if (hasRow() && position == itemCount - 1)
        {
            val networkViewType = NETWORK_VIEW_TYPE
            networkViewType
        }
        else
        {
            val movieViewType = MOVIE_VIEW_TYPE
            movieViewType
        }
    }

    private fun hasRow() : Boolean = state != null && state != State.LOADED

    override fun getItemCount() : Int = super.getItemCount() + if(hasRow()) 1 else 0
    fun setNet(b: State) {
        val sebelum : State? = this.state
        val had = hasRow()
        this.state = b
        val has = hasRow()

        if (has != had) {
            if (has) {
                notifyItemInserted(super.getItemCount()) 
            } else {                                       
                notifyItemRemoved(super.getItemCount())
            }
        } else
        {
            if (has && sebelum != b) {
                notifyItemChanged(itemCount - 1)
            }
        }

    }
    class MovieViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val judul: TextView = view.judulItem
        private val populer: TextView = view.populerItem
        private val img : ImageView = view.imgItem

        fun binding(m: MovieKorea?,context: Context) {
           judul.text = m?.title
            populer.text =  m?.popularity.toString()

            Glide.with(itemView.context)
                .load(POSTER_BASE_URL + m?.posterPath)
                .into(img)

            itemView.setOnClickListener {
                val i = Intent(context , DetailActivity::class.java)
                i.putExtra("id" , m?.id)
                context.startActivity(i)
            }

        }

    }

    class NetworkViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        fun binding(state : State)
        {
            if (state == State.LOADING)
            {
                itemView.progress.visibility = View.VISIBLE
            }
            else
            {
                itemView.progress.visibility = View.GONE
            }

            when (state)
            {
                State.ERROR ->
                {
                    itemView.progress.visibility = View.VISIBLE
                    itemView.errorT.text = state.msg
                }
                State.END ->
                {
                    itemView.errorT.visibility = View.VISIBLE
                    itemView.errorT.text = state.msg
                }
                else ->
                {
                    itemView.errorT.visibility = View.GONE
                }
            }
        }
    }

}




