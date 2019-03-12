@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.vitalii.yellowsjoblog.api

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ServerConnection {

    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"
    private val URL_DEV = "http://joblog.yellows.pl/"
    private val gsonConverter = GsonConverterFactory.create()
    private val httpClient = OkHttpClient.Builder()

    private val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)

    fun createService(token:String):JobLogService{
        val authInterceptor = AuthenticationInterceptor(token)

        httpClient
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)


        val builder = Retrofit.Builder()
            .baseUrl(URL_DEV)
            .addConverterFactory(gsonConverter)
            .client(httpClient.build())

        val retrofit = builder.build()

        return retrofit.create(JobLogService::class.java)
    }

    fun createService(username:String, password:String):JobLogService{
        httpClient.addInterceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder().header("Authorization", Credentials.basic(username,password))
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }
            .addInterceptor(logging)

        val builder = Retrofit.Builder()
            .baseUrl(URL_DEV)
            .addConverterFactory(gsonConverter)
            .client(httpClient.build())

        val retrofit = builder.build()

        return retrofit.create(JobLogService::class.java)
    }
}