package com.yassir.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.yassir.test.databinding.ActivityDetailsBinding
import com.yassir.test.mvvm.MovieViewModel
import com.yassir.test.states.UiStates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private var movieId = 0
    private lateinit var binding : ActivityDetailsBinding
    private val movieViewModel : MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        intent?.let {
            movieId = it.getIntExtra("id",0)
        }

        movieViewModel.getMoviesDetails(movieId,Utils.API_KEY,"en-US")
        lifecycleScope.launch {
            movieViewModel.flow.collectLatest {
                when(it){
                  is UiStates.LOADING -> {
                      binding.progressBar.visibility = View.VISIBLE
                  }
                  is UiStates.SUCCESS -> {
                      binding.progressBar.visibility = View.GONE
                      Picasso.get().load(Utils.IMAGE_URL + it.data.poster_path).into(binding.movieImage)
                      binding.title.text = it.data.title
                      binding.date.text  = it.data.release_date
                      binding.overView.text = it.data.overview
                  }
                  is UiStates.ERROR -> {
                      binding.errorTxt.visibility = View.GONE
                      binding.errorTxt.text = "Could not fetch data .."
                  }
                  else -> {}
                }
            }
        }
    }
}