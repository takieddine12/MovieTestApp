package com.yassir.test.mvvm

import com.yassir.test.auth.AuthService
import com.yassir.test.models.details.DetailsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private var authService: AuthService
) {

    suspend fun getMovieDetails(movieId : Int , apiKey : String , language : String) : Flow<DetailsModel> {
        return flow {
            emit(authService.getMovieDetails(movieId,apiKey,language))
        }
    }
}