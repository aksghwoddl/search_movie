package com.lee.searchmovie.di

import com.lee.searchmovie.BuildConfig
import com.lee.searchmovie.common.NetworkConst
import com.lee.searchmovie.data.api.RestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Provides를 통한 주입을 하기위한 Module
 * **/
@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        return if(BuildConfig.DEBUG){ // BuildConfig가 Debug일때는 Interceptor를 추가한다.
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else { // BuildConfig가 Release일때는 Interceptor없이 OkHttp를 생성한다.
            OkHttpClient.Builder().build()
        }
    }

    @Provides
    @Singleton
    fun provideRestApi(okHttpClient: OkHttpClient) : RestApi {
        return Retrofit.Builder()
            .baseUrl(NetworkConst.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApi::class.java)
    }
}