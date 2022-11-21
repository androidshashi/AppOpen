package com.shashifreeze.appopen.network.api

import com.shashifreeze.appopen.apputils.Constants.GET_KEYWORD_DATA
import com.shashifreeze.appopen.network.response.UrlResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for Home page calls
 */
interface UrlDataApi {
     companion object{

         //clients
         const val CLIENT_YT = "youtube"
         const val CLIENT_CHROME = "chrome"

         //data sources
         const val DS_YT = "yt"
         const val DS_GOOGLE = ""
     }
    /**
     * @param: qrcode
     * @return: QRScanResponse
     * @author: Shashi
     * Get keyword data
     */
    @GET(GET_KEYWORD_DATA)
    suspend fun getKeywordData(
        @Query("hl") hl: String="en",//language
        @Query("client") client: String=CLIENT_YT,//client can be changed to any browser
        @Query("hjson") hjson: String = "t",
        @Query("cp") cp: Int=1,
        @Query("q") q: String,//query
        @Query("alt") alt: String="json",//alternate
        @Query("ds") dataSource: String=DS_GOOGLE,//alternate
    ): UrlResponse

}