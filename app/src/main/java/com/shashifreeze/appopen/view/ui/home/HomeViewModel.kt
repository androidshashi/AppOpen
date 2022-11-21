package com.shashifreeze.appopen.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashifreeze.appopen.apputils.ConsumableLiveData
import com.shashifreeze.appopen.network.NetworkResource
import com.shashifreeze.appopen.network.response.UrlResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepository) : ViewModel() {

    /**
     * Fetch keyword data
     */
    private var _keywordResponseLiveData = ConsumableLiveData<NetworkResource<UrlResponse>>(true)
    val keywordResponseLiveData: LiveData<NetworkResource<UrlResponse>> get() = _keywordResponseLiveData

    fun getKeywordData(query:String, ds:String) {
        _keywordResponseLiveData.postValue(NetworkResource.Loading)

        viewModelScope.launch {
            _keywordResponseLiveData.postValue(repo.getKeywordData(query, ds = ds))
        }
    }
}