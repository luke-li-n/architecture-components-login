package com.cloud.coroutines.network

import com.cloud.coroutines.App
import com.cloud.coroutines.BuildConfig
import com.cloud.coroutines.data.model.LoggedInUser
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<LoggedInUser>

    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any>

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com"

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().also {
                it.level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
            val context = App.instance.applicationContext
            val cookieJar =
                PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .cookieJar(cookieJar)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}