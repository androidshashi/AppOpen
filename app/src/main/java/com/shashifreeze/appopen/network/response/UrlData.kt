package com.shashifreeze.appopen.network.response


import com.google.gson.annotations.SerializedName

data class UrlData(
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("hits")
    val hits: Int,
    @SerializedName("longUrl")
    val longUrl: String,
    @SerializedName("shortCode")
    val shortCode: String,
    @SerializedName("siteKey")
    val siteKey: String?,
    @SerializedName("totalLinks")
    val totalLinks: Int,
    @SerializedName("type")
    val type: String
)