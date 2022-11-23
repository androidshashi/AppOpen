package com.shashifreeze.appopen.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 *@author = Shashi
 *@date = 23/07/21
 *@description = This class handles
 */
@Entity(tableName = "history_table", indices = [Index(value = ["shortUrl"], unique = true)])
data class HistoryData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "longUrl") val longUrl: String,
    @ColumnInfo(name = "shortUrl") val shortUrl: String)
