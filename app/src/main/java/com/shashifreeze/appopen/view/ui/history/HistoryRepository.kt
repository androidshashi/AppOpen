package com.shashifreeze.appopen.view.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashifreeze.appopen.base.BaseRepository
import com.shashifreeze.appopen.database.dao.HistoryDao
import com.shashifreeze.appopen.database.entity.HistoryData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author = Shashi
 *@date = 18/08/21
 *@description = This class handles
 */
@Singleton
class HistoryRepository @Inject constructor(private val dao: HistoryDao) : BaseRepository() {

    var getAllHistoryLiveData: LiveData<List<HistoryData>> = MutableLiveData()

    suspend fun save(
        shortUrl: String,
        longUrl: String,
    ) = withContext(IO) {

        val historyData = HistoryData(
            shortUrl = shortUrl,
            longUrl = longUrl
        )
        dao.insert(historyData)
    }

    suspend fun getAllHistory() = withContext(IO)
    {
        dao.getHistory()
    }

    suspend fun deleteAllHistory() = withContext(IO)
    {
        dao.deleteAllHistory()
    }

    suspend fun deleteAHistory(id: Long)= withContext(IO)
    {
        dao.deleteAHistory(id)
    }

    suspend fun saveHistory(shortUrl: String, longUrl:String)= withContext(IO){
        dao.insert(HistoryData(shortUrl = shortUrl, longUrl = longUrl))
    }
}