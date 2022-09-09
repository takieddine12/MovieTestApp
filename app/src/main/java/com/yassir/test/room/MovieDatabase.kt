package com.yassir.test.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yassir.test.models.keys.RemoteKey
import com.yassir.test.models.Result

@Database(entities = [Result::class,RemoteKey::class], version = 1, exportSchema = false)
abstract class  MovieDatabase : RoomDatabase(){

    abstract fun getMoviesDao() : MovieDao
    abstract fun getRemoteKeysDao() : RemoteKeyDao
}