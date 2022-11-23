package com.shashifreeze.appopen.di

import android.content.Context
import com.shashifreeze.appopen.database.AppDatabase
import com.shashifreeze.appopen.database.dao.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
@Author: Shashi
@Date: 19-03-2021
@Description: Provides Objects in the application scope
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * @param: ApplicationContext
     * @return: AppDatabase
     * @author: Shashi
     * Creates and provides AppDatabase object reference */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase(context)
    }
    /**
     * @param: AppDatabase
     * @return: HistoryDao
     * @author: Shashi
     * Creates and provides HistoryDao reference */
    @Provides
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.historyDao()
    }

}