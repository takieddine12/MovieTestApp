package com.yassir.test.room

import android.icu.text.CaseMap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yassir.test.models.keys.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM RemoteKey WHERE movieId = :id")
    suspend fun remoteKeysByMoviesId(id : Long): RemoteKey?

    @Query("DELETE FROM RemoteKey")
    fun clearRemoteKeys()
}