package com.shashifreeze.appopen.network.response
import com.google.gson.annotations.SerializedName
import com.shashifreeze.appopen.network.api.UrlDataApi.Companion.URL_TYPE_IG
import com.shashifreeze.appopen.network.api.UrlDataApi.Companion.URL_TYPE_WEB
import com.shashifreeze.appopen.network.api.UrlDataApi.Companion.URL_TYPE_YT

data class UrlResponse(
    @SerializedName("urlData")
    val urlData: UrlData
)