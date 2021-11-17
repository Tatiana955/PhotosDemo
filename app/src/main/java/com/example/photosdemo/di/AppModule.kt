package com.example.photosdemo.di

import android.content.Context
import androidx.room.Room
import com.example.photosdemo.data.Repository
import com.example.photosdemo.data.local.PhotosDatabase
import com.example.photosdemo.data.remote.ApiService
import com.example.photosdemo.data.remote.BASE_URL
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.example.photosdemo.viewmodel.PhotosViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(): ApiService {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PhotosDatabase {
        var appDatabase: PhotosDatabase? = null
        return appDatabase ?: synchronized(this) {
            val database: PhotosDatabase = Room.databaseBuilder(
                context.applicationContext,
                PhotosDatabase::class.java, "photos_db"
            ).build()
            appDatabase = database
            database
        }
    }

    @Singleton
    @Provides
    fun provideRepository(
        service: ApiService,
        database: PhotosDatabase
    ): Repository {
        return Repository(service, database)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(
        repository: Repository
    ): PhotosViewModelFactory {
        return PhotosViewModelFactory(repository)
    }

    @Singleton
    @Provides
    fun provideViewModel(
        repository: Repository
    ): PhotosViewModel {
        return PhotosViewModel(repository)
    }
}