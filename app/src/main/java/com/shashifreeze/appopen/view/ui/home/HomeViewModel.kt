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
     * create short link
     */
    private var _createShortLinkLiveData = ConsumableLiveData<NetworkResource<UrlResponse>>(true)
    val createShortLinkLiveData: LiveData<NetworkResource<UrlResponse>> get() = _createShortLinkLiveData

    fun createShortLink(longUrl:String) {
        _createShortLinkLiveData.postValue(NetworkResource.Loading)

        viewModelScope.launch {
            _createShortLinkLiveData.postValue(repo.createShortLink(longUrl))
        }
    }
}