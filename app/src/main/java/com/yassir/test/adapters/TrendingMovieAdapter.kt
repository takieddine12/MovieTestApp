package com.yassir.test.adapters

import android.database.DatabaseUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yassir.test.R
import com.yassir.test.databinding.MovieRowsLayoutBinding
import com.yassir.test.listeners.MovieListener
import com.yassir.test.models.Result

class TrendingMovieAdapter(var listener : MovieListener) : PagingDataAdapter<Result,TrendingMovieAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(var binding : MovieRowsLayoutBinding, ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           DataBindingUtil.inflate(LayoutInflater.from(parent.context),
           R.layout.movie_rows_layout,parent,false)
       )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val movies = getItem(position)
       holder.binding.model = movies
       holder.binding.listener = listener
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem == newItem
        }
    }

}