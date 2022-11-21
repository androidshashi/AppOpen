package com.shashifreeze.appopen.view.ui.home

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
class HomeRepository @Inject constructor(private val keywordDataApi: UrlDataApi) : BaseRepository() {

    suspend fun getKeywordData(query:String, ds:String)  = withContext(Dispatchers.IO)
    {
        safeApiCall { keywordDataApi.getKeywordData(q = query, dataSource = ds) }
    }

}