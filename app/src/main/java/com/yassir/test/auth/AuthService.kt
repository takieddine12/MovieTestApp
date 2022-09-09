package com.yassir.test.auth

import com.yassir.test.models.MovieModel
import com.yassir.test.models.details.DetailsModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {


    @GET("discover/movie?")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey : String,
        @Query("language") language : String,
        @Query("sort_by") sort_by : String,
        @Query("include_adult") include_adult : Boolean,
        @Query("include_video") include_video : Boolean,
        @Query("page") page : Int,
        @Query("with_watch_monetization_types") monetization : String,
    ) : MovieModel


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId : Int ,
        @Query("api_key") apiKey : String,
        @Query("language") language : String
    ) : DetailsModel
}