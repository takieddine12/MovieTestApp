package com.yassir.test.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yassir.test.models.Result

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Result>)

    @Query("SELECT * FROM Result Order by id")
    fun pagingSource(): PagingSource<Int, Result>

    @Query("DELETE FROM Result")
    suspend fun clearAll()
}