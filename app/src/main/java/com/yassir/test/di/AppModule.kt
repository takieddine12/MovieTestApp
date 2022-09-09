package com.yassir.test.di

import android.content.Context
import androidx.room.Room
import com.yassir.test.Utils
import com.yassir.test.auth.AuthService
import com.yassir.test.room.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun getAuthInstance() : AuthService {
//        val httpLoggingInterceptor  = HttpLoggingInterceptor()
//        val interceptor = httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(interceptor)
//            .build()
        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
           // .client(okHttpClient)
            .build().create(AuthService::class.java)
    }


    @Singleton
    @Provides
    fun getDatabaseInstance(@ApplicationContext context: Context) =
        Room.databaseBuilder(context,MovieDatabase::class.java,"movie.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun getMovieDao(database: MovieDatabase) = database.getMoviesDao()

    @Singleton
    @Provides
    fun getRemoteKeysDao(database: MovieDatabase) = database.getRemoteKeysDao()
}