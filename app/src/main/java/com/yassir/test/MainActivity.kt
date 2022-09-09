package com.yassir.test

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yassir.test.adapters.LoadingStateAdapter
import com.yassir.test.adapters.TrendingMovieAdapter
import com.yassir.test.databinding.ActivityMainBinding
import com.yassir.test.listeners.MovieListener
import com.yassir.test.mvvm.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var movieAdapter: TrendingMovieAdapter
    private val movieViewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager  = layoutManager
        binding.recyclerView.setHasFixedSize(true)
        movieAdapter = TrendingMovieAdapter(object : MovieListener{
            override fun onMovieSelected(movieId: Int) {
                Intent(this@MainActivity,DetailsActivity::class.java).apply {
                    putExtra("id",movieId)
                    startActivity(this)
                }
            }
        })
        binding.recyclerView.adapter = movieAdapter

        binding.recyclerView.adapter = movieAdapter.withLoadStateHeaderAndFooter(
            footer = LoadingStateAdapter(movieAdapter),
            header = LoadingStateAdapter(movieAdapter)
        )


        lifecycleScope.launch {
            movieViewModel.getPagedTrendingMovies().collectLatest { data ->
                movieAdapter.submitData(data)
            }

        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}