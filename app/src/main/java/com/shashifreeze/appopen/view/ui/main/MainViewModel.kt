package com.shashifreeze.appopen.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashifreeze.appopen.apputils.ConsumableLiveData
import com.shashifreeze.appopen.network.NetworkResource
import com.shashifreeze.appopen.network.response.AppDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @constructor: private val repository: MainRepository
 * @author: Shashi
 * View model for Main Activity*/
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    /**
     * Fetch statuses
     */
    private var _appDataLiveData = ConsumableLiveData<NetworkResource<AppDataResponse>>(true)
    val appDataLiveData: LiveData<NetworkResource<AppDataResponse>> get() = _appDataLiveData

    fun getAppData(packageName:String) {

        _appDataLiveData.postValue(NetworkResource.Loading)

        viewModelScope.launch {
            _appDataLiveData.postValue(repository.getAppData(packageName))
        }
    }

}