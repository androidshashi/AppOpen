package com.shashifreeze.appopen.di

import com.shashifreeze.appopen.network.RemoteDataSource
import com.shashifreeze.appopen.network.api.AppDataApi
import com.shashifreeze.appopen.network.api.UrlDataApi
import com.shashifreeze.appopen.view.ui.home.HomeRepository
import com.shashifreeze.appopen.view.ui.main.MainRepository
import com.shashifreeze.appopen.view.ui.search.SearchRepository
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
object AppModule {
    /**
     * @param: NA
     * @return: RemoteDataSource
     * @author: Shashi
     * Creates and provides RemoteDataSource object reference */
    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSource()

    /**
     * @param: NA
     * @return: CategoriesRepository
     * @author: Shashi
     * Creates and provides CategoriesRepository object reference
     */
    @Provides
    @Singleton
    fun provideMainRepository(api: AppDataApi): MainRepository = MainRepository(api)

    /**
     * @param: NA
     * @return: HomeRepository
     * @author: Shashi
     * Creates and provides HomeRepository object reference */
    @Provides
    @Singleton
    fun provideHomeRepository(api: UrlDataApi): HomeRepository = HomeRepository(api)

    /**
     * @param: NA
     * @return: HomeRepository
     * @author: Shashi
     * Creates and provides SearchRepository object reference */
    @Provides
    @Singleton
    fun provideSearchRepository(api: UrlDataApi): SearchRepository = SearchRepository(api)


}