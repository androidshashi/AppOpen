package com.shashifreeze.appopen.di

import com.shashifreeze.appopen.network.RemoteDataSource
import com.shashifreeze.appopen.network.RemoteDataSource.Companion.APP_DATA_BASE_URL
import com.shashifreeze.appopen.network.api.AppDataApi
import com.shashifreeze.appopen.network.api.UrlDataApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
@Author: Shashi
@Date: 19-03-2021
@Description: Provides Objects in the application scope
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * @param: ApplicationContext
     * @return: AppDatabase
     * @author: Shashi
     * Creates and provides AppDatabase object reference */
    @Singleton
    @Provides
    fun provideAppDataApi( remoteDataSource: RemoteDataSource): AppDataApi {
        return remoteDataSource.buildApi(baseUrl = APP_DATA_BASE_URL, api = AppDataApi::class.java)
    }

    /**
     * @param: ApplicationContext
     * @return: AppDatabase
     * @author: Shashi
     * Creates and provides AppDatabase object reference */
    @Singleton
    @Provides
    fun provideUrlDataApi( remoteDataSource: RemoteDataSource): UrlDataApi {
        return remoteDataSource.buildApi( api = UrlDataApi::class.java)
    }

}