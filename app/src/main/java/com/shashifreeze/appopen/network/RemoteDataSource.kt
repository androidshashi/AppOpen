package com.shashifreeze.appopen.network

import com.shashifreeze.appopen.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
class RemoteDataSource {

    companion object {
        private val BASE_URL = "https://nextnearby.com/api/"
        private val MAP_BASE_URL = "https://maps.googleapis.com/maps/api/"
    }

    /**@param :  (api:Class<Api>,authToken:String? =null)
     * @author: Shashi
     * @return : Api
     * Build Api instance using retrofit  */
    @Singleton
    fun <Api> buildApi(api: Class<Api>, authToken: String? = null): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(authToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    /**@param :  authToken:String? = null
     * @author: Shashi
     * @return : OkHttpClient
     * Provides OkHttpClient instance  */
    @Singleton
    private fun getOkHttpClient(authToken: String? = null) =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Authorization", "Bearer $authToken")
                    it.addHeader("Content-Type", "application/json")
                    it.addHeader("Accept", "application/json")
                }.build())
            }
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build()

    /**@param :  (api:Class<Api>)
     * @author: Shashi
     * @return : Api
     * Build Google map Api instance using retrofit  */
    @Singleton
    fun <Api> buildMapApi(api: Class<Api>): Api {
        return Retrofit.Builder()
            .baseUrl(MAP_BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}