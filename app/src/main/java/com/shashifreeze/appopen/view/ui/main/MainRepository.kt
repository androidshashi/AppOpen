package com.shashifreeze.appopen.view.ui.main

import com.shashifreeze.appopen.base.BaseRepository
import com.shashifreeze.appopen.network.api.AppDataApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository@Inject constructor(private val ap: AppDataApi)  : BaseRepository() {
    suspend fun getAppData(packageName:String)  = withContext(Dispatchers.IO)
    {
        safeApiCall { ap.getAppData(packageName) }
    }
}