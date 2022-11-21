package com.shashifreeze.appopen.network.api

import com.shashifreeze.appopen.apputils.Constants.GET_APP_DATA
import com.shashifreeze.appopen.network.response.AppDataResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Interface for Home page calls
 */
interface AppDataApi {
    /**
     * @param: qrcode
     * @return: QRScanResponse
     * @author: Shashi
     * Get shop id from qr scanned code
     */
    @FormUrlEncoded
    @POST(GET_APP_DATA)
    suspend fun getAppData(@Field("package_name") packageName:String, @Field("token") appToken: String = "fadsfnklafdbkjwq35325zxxckjz2345235xsfghsghfs"): AppDataResponse

}