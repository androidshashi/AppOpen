package com.shashifreeze.appopen.network.api

import com.shashifreeze.appopen.apputils.Constants.CREATE_SHORT_LINK
import com.shashifreeze.appopen.apputils.Constants.GET_SHORT_LINK_INFO
import com.shashifreeze.appopen.network.response.UrlResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interface for Home page calls
 */
interface UrlDataApi {
    /**
     * @param:
     * @return:
     * @author: Shashi
     * Get URL data
     */
    companion object{
        const val URL_TYPE_WEB = "web"
        const val URL_TYPE_YT = "yt"
        const val URL_TYPE_IG = "ig"
    }
    @GET(GET_SHORT_LINK_INFO)
    suspend fun getShortCodeInfo(
        @Query("short_code") shortCode:String,//short code
        @Query("type") type: String,//url type web/yt/ig
    ): UrlResponse

    @POST(CREATE_SHORT_LINK)
    suspend fun createShortLink(
        @Query("long_url") longUrl:String,//short code
    ): UrlResponse

}