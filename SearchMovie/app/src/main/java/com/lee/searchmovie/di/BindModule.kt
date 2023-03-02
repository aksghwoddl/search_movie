package com.lee.searchmovie.di

import com.lee.searchmovie.data.repository.MainRepositoryImpl
import com.lee.searchmovie.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds를 통한 주입을 하기 위한 Module
 * **/
@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    @Singleton
    abstract fun bindRepository(mainRepositoryImpl: MainRepositoryImpl) : MainRepository
}