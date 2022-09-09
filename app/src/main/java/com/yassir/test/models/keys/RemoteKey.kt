package com.yassir.test.models.keys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    var movieId : Long? ,
    val prevKey: Int? = null ,
    val nextKey: Int? = null)
{

    @PrimaryKey
    var id : Long? = null
}