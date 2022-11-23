package com.shashifreeze.appopen.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shashifreeze.appopen.apputils.Constants.DATABASE_NAME
import com.shashifreeze.appopen.apputils.Constants.DATABASE_VERSION
import com.shashifreeze.appopen.database.dao.HistoryDao
import com.shashifreeze.appopen.database.entity.HistoryData

@Database(entities = [HistoryData::class],version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
       @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}