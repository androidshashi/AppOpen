package com.shashifreeze.appopen.network.response
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AppData(
    @SerializedName("appIconUrl")
    val appIconUrl: String,
    @SerializedName("appPackageName")
    val appPackageName: String,
    @SerializedName("appTitle")
    val appTitle: String,
    @SerializedName("message")
    val message: String
)