package com.shashifreeze.appopen.view.ui.search

import com.shashifreeze.appopen.base.BaseRepository
import com.shashifreeze.appopen.network.api.UrlDataApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author = Shashi
 *@date = 18/08/21
 *@description = This class handles
 */
@Singleton
class SearchRepository @Inject constructor(private val api: UrlDataApi) : BaseRepository() {

    suspend fun getShortCodeInfo(shortCode:String, type:String)  = withContext(Dispatchers.IO)
    {
        safeApiCall { api.getShortCodeInfo(shortCode, type) }
    }

    suspend fun createShortLink(longUrl:String)  = withContext(Dispatchers.IO)
    {
        safeApiCall { api.createShortLink(longUrl) }
    }

}