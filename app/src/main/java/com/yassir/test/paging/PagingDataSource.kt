package com.yassir.test.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yassir.test.Utils
import com.yassir.test.auth.AuthService
import com.yassir.test.models.MovieModel
import com.yassir.test.models.Result

class PagingDataSource(
    private var authService: AuthService
) : PagingSource<Int,Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val page = params.key ?: 1
            val response = authService.getTrendingMovies(Utils.API_KEY,"en-US","popularity.desc",
                include_adult = false, include_video = false,page,"flatrate")
            LoadResult.Page(
                data = response.results,
                prevKey = if(page == 1) null else page - 1,
                nextKey = page + 1
            )
        }catch ( ex : Exception){
            LoadResult.Error(
                throwable = ex
            )
        }
    }
}