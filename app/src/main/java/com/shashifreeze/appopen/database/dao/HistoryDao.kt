package com.shashifreeze.appopen.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shashifreeze.appopen.database.entity.HistoryData

/**
 *@author = Shashi
 *@date = 23/07/21
 *@description = This class handles all the queries of room database
 */

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: HistoryData)

    @Update
    fun update(data: HistoryData)

    @Query("SELECT * FROM history_table")
    fun getHistory(): LiveData<List<HistoryData>>

    /**
     * Deletes all history
     */
    @Query("DELETE FROM history_table")
    fun deleteAllHistory() : Int

    /**
     * Deletes a history
     */
    @Query("DELETE FROM history_table WHERE id=(:historyId)")
    fun deleteAHistory(historyId: Long):Int

}