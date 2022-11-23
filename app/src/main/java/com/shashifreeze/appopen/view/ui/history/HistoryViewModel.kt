package com.shashifreeze.appopen.view.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashifreeze.appopen.database.entity.HistoryData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repo: HistoryRepository) : ViewModel() {

    suspend fun getHistory(): LiveData<List<HistoryData>> {
        return repo.getAllHistory()
    }

     suspend fun saveHistory(shortUrl:String, longUrl:String) {
        return repo.saveHistory(shortUrl, longUrl)
    }

     fun deleteHistory(id:Long) {
        viewModelScope.launch {
            repo.deleteAHistory(id)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            repo.deleteAllHistory()
        }
    }

}