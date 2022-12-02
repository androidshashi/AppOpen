package com.shashifreeze.appopen.network.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.shashifreeze.appopen.network.response.AppData
@Keep
data class AppDataResponse(
    @SerializedName("appData")
    val appData: AppData,
    @SerializedName("appError")
    val appError: String?,
    @SerializedName("responsecode")
    val responseCode: Int
)