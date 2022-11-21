package com.shashifreeze.appopen.network.response


import com.google.gson.annotations.SerializedName

data class UrlResponse(
    @SerializedName("urlData")
    val urlData: UrlData
)