package com.shashifreeze.appopen.view.ui.search

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
class SearchViewModel @Inject constructor(private val repo: SearchRepository) : ViewModel() {

    /**
     * Fetch keyword data
     */
    private var _shortCodeInfoLiveData = ConsumableLiveData<NetworkResource<UrlResponse>>(true)
    val shortCodeInfoLiveData: LiveData<NetworkResource<UrlResponse>> get() = _shortCodeInfoLiveData

    fun getShortCodeInfo(shortCode:String, type:String) {
        _shortCodeInfoLiveData.postValue(NetworkResource.Loading)

        viewModelScope.launch {
            _shortCodeInfoLiveData.postValue(repo.getShortCodeInfo(shortCode,type))
        }
    }

}