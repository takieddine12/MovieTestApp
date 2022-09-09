package com.yassir.test.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yassir.test.Utils
import com.yassir.test.auth.AuthService
import com.yassir.test.models.Result
import com.yassir.test.models.keys.RemoteKey
import com.yassir.test.room.MovieDatabase
import timber.log.Timber
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    var authService: AuthService,
    var database: MovieDatabase
) : RemoteMediator<Int,Result>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        return  try {
            val currentPage = when(loadType){
               LoadType.REFRESH -> {
                   val remoteKeys = getRemoteKeyClosestToCurrentPosition(state = state)
                   remoteKeys?.nextKey?.minus(1) ?: Utils.START_INDEX
               }
               LoadType.PREPEND -> {
                   val remoteKeys = getRemoteKeyForFirstTime(state = state)
                   val prevPage = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                   prevPage
               }

               LoadType.APPEND -> {
                   val remoteKeys = getRemoteKeyForLastTime(state = state)
                   val nextPage = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                   nextPage
               }
           }
            val response = authService.getTrendingMovies(Utils.API_KEY,"en-US","popularity.desc",
                include_adult = false, include_video = false, currentPage ,"flatrate")

            val endOfPaginationReached = response.results.isEmpty()

            val prevKey = if (currentPage == 1) Utils.START_INDEX else currentPage - 1
            val nextKey = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                // if refreshing, clear table and start over
                if (loadType == LoadType.REFRESH) {
                    database.getRemoteKeysDao().clearRemoteKeys()
                    database.getMoviesDao().clearAll()
                }

                val keys = response.results.map {
                    RemoteKey(movieId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }

                database.getRemoteKeysDao().insertAll(keys)
                database.getMoviesDao().insertAll(response.results)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (ex : Exception){
            MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int,Result>
    ): RemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { id ->
                database.getRemoteKeysDao().remoteKeysByMoviesId(id = id.toLong())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstTime(
        state: PagingState<Int, Result>
    ): RemoteKey? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                database.getRemoteKeysDao().remoteKeysByMoviesId(id = it.id.toLong())
            }
    }

    private suspend fun getRemoteKeyForLastTime(state: PagingState<Int, Result>): RemoteKey? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                database.getRemoteKeysDao().remoteKeysByMoviesId(id = it.id.toLong())
            }
    }
}