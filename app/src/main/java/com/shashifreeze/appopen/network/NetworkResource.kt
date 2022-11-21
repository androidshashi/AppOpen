package com.shashifreeze.appopen.network

import okhttp3.ResponseBody

sealed class NetworkResource<out T> {

    //data class to handle success of api
    //This class is generic so that all the success responses can be handled
    data class Success<T>(var value: T) : NetworkResource<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val message:String?=null
    ): NetworkResource<Nothing>()

    object Loading : NetworkResource<Nothing>()
}
