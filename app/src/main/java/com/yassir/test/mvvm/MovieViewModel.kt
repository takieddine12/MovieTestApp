package com.yassir.test.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yassir.test.Utils
import com.yassir.test.auth.AuthService
import com.yassir.test.mediator.MovieRemoteMediator
import com.yassir.test.models.Result
import com.yassir.test.paging.PagingDataSource
import com.yassir.test.room.MovieDao
import com.yassir.test.room.MovieDatabase
import com.yassir.test.room.RemoteKeyDao
import com.yassir.test.states.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val authService: AuthService,
    private val movieRepository: MovieRepository,
    private val database: MovieDatabase
) : ViewModel() {

    // STATE FOR DETAILS SCREEN
    private val _statesFlow : MutableStateFlow<UiStates>  = MutableStateFlow(UiStates.EMPTY)
    val flow : StateFlow<UiStates> get() = _statesFlow


    @OptIn(ExperimentalPagingApi::class)
    fun getPagedTrendingMovies(): Flow<PagingData<Result>> {
        return Pager(
            config =  PagingConfig(pageSize = Utils.NETWORK_PAGE_SIZE),
            remoteMediator = MovieRemoteMediator(authService,database),
            pagingSourceFactory = {
                PagingDataSource(authService)
            },
        ).flow
    }

    fun getOfflinePagedMovies() : Flow<PagingData<Result>> {
        return  Pager(
            config =  PagingConfig(pageSize = Utils.NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                database.getMoviesDao().pagingSource()
            }
        ).flow

    }

    fun getMoviesDetails(movieId : Int,apiKey : String, language : String){
        viewModelScope.launch {
            try {
                _statesFlow.value = UiStates.LOADING
                movieRepository.getMovieDetails(movieId,apiKey,language).collectLatest {
                    _statesFlow.value = UiStates.SUCCESS(it)
                }
            }catch (ex : Exception){
                _statesFlow.value  = UiStates.ERROR(ex.message!!)
            }
        }
    }
}