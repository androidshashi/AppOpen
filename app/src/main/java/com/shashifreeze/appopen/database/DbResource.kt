package com.shash.keywordtool.database

sealed class DbResource<out T> {

    //data class to handle success of api
    //This class is generic so that all the success responses can be handled
    data class Success<T>(var value: T) : DbResource<T>()

    data class Failure(
        val isIOError: Boolean,
        val errorCode: Int?=null,
        val errorMsg:String?=null,
    ): DbResource<Nothing>()

    object Loading : DbResource<Nothing>()
}
