package com.shashifreeze.appopen.base

import android.util.Log
import com.shashifreeze.appopen.network.NetworkResource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/***
 * @author: Shashi
 * Base repository for all the repository   */
abstract class BaseRepository {

    /**
     * @param: apiCall: suspend () -> T
     * @return: Resource<T>
     * @author: Shashi
     * Makes safe API call and wraps the response in Resource<T> */
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResource<T> {
        return withContext(IO) {
            try {
                NetworkResource.Success(apiCall.invoke())

            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Log.d("BaseRepository","BaseRepository:${throwable.response()}\n${throwable.code()}")
                        NetworkResource.Failure(isNetworkError = false, errorCode = throwable.code(), errorBody = throwable.response()?.errorBody(),message = throwable.message)
                    }
                    else -> {
                        NetworkResource.Failure(isNetworkError = true, errorCode = null, errorBody = null,message = throwable.message)
                    }
                }
            }
        }
    }

}