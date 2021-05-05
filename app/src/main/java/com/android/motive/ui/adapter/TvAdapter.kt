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
import com.android.motive.data.source.local.entity.TvKorea
import com.android.motive.ui.detailTv.DetailTvActivity
import com.android.motive.utils.Constant
import com.android.motive.utils.State
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_net.view.*

class TvAdapter(private val context: Context) : PagedListAdapter<TvKorea , RecyclerView.ViewHolder>(TCallback()) {
    private var state : State? = null

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : RecyclerView.ViewHolder {

        return if (viewType == Constant.NETWORK_VIEW_TYPE) {
            val myView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_list , parent , false)
            NetworkViewHolder(myView)
        } else{
            val myView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_net , parent , false)
            TvViewHolder(myView)
        }

    }

    override fun onBindViewHolder(holder :RecyclerView.ViewHolder , position :Int) {
        if(getItemViewType(position) == Constant.NETWORK_VIEW_TYPE) {
            (holder as NetworkViewHolder).binding(state)
        }else{
            (holder as TvViewHolder).binding(getItem(position) , this.context)
        }

    }



    class TCallback : DiffUtil.ItemCallback<TvKorea>() {
        override fun areItemsTheSame(oldItem: TvKorea, newItem: TvKorea): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvKorea, newItem: TvKorea): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position : Int) : Int
    {
        return if (hasRow() && position == itemCount - 1)
        {
            val networkViewType = Constant.NETWORK_VIEW_TYPE
            networkViewType
        }
        else
        {
            val movieViewType = Constant.MOVIE_VIEW_TYPE
            movieViewType
        }
    }

    private fun hasRow() : Boolean = state != null && state != State.LOADED

    override fun getItemCount() : Int = super.getItemCount() + if(hasRow()) 1 else 0

    class TvViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val judul: TextView = view.judulItem
        private val populer: TextView = view.populerItem
        private val img : ImageView = view.imgItem

        fun binding(m: TvKorea?,context: Context) {
            judul.text = m?.name
            populer.text =  m?.popularity.toString()

            Glide.with(itemView.context)
                .load(Constant.POSTER_BASE_URL + m?.posterPath)
                .into(img)

            itemView.setOnClickListener {
                val i = Intent(context , DetailTvActivity::class.java)
                i.putExtra("id" , m?.id)
                context.startActivity(i)
            }

        }

    }

    class NetworkViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        fun binding(state : State?)
        {
            if (state != null && state == State.LOADING)
            {
                itemView.progress.visibility = View.VISIBLE
            }
            else
            {
                itemView.progress.visibility = View.GONE
            }

            if (state != null && state == State.ERROR)
            {
                itemView.progress.visibility = View.VISIBLE
                itemView.errorT.text = state.msg
            }
            else if (state != null && state == State.END)
            {
                itemView.errorT.visibility = View.VISIBLE
                itemView.errorT.text = state.msg
            }
            else
            {
                itemView.errorT.visibility = View.GONE
            }
        }
    }

    fun setNetTv(b: State) {
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

}


