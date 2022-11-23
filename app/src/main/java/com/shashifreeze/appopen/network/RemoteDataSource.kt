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
        private const val BASE_URL = "https://appopen.me/api/"
        const val APP_DATA_BASE_URL = "https://earningdesire.com/apps-api/"
    }

    /**@param :  (api:Class<Api>,authToken:String? =null)
     * @author: Shashi
     * @return : Api
     * Build Api instance using retrofit  */
    @Singleton
    fun <Api> buildApi(api: Class<Api>, authToken: String? = null,baseUrl:String=BASE_URL): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
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

}