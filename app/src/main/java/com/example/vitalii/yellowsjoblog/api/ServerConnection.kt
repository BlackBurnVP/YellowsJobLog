@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.vitalii.yellowsjoblog.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerConnection {

    private var service:JobLogService? = null
     var retrofit:Retrofit? = null
    private var reportsObject:Callback<List<ReportsPOKO>>? = null
    private var reports:Call<List<ReportsPOKO>>? = null
    var usersObject:Callback<List<Users>>? = null
    var users:Call<List<Users>>? = null
    private var projectsObject:Callback<List<ProjectsPOKO>>? = null
    private var projects:Call<List<ProjectsPOKO>>? = null
    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"

    fun serverConnect(filter:MutableMap<String,String>){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        reportsObject = object : Callback<List<ReportsPOKO>> {
            override fun onFailure(call: Call<List<ReportsPOKO>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<ReportsPOKO>>, response: Response<List<ReportsPOKO>>) {

            }

        }

        usersObject = object : Callback<List<Users>> {
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {

            }

        }

        projectsObject = object : Callback<List<ProjectsPOKO>> {
            override fun onFailure(call: Call<List<ProjectsPOKO>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<ProjectsPOKO>>, response: Response<List<ProjectsPOKO>>) {

            }

        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit?.create(JobLogService::class.java)
        reports = service?.getTasks(filter)
        reports?.enqueue(reportsObject)
        users = service?.getUsers()
        users?.enqueue(usersObject)
        projects = service?.getProjects()
        projects?.enqueue(projectsObject)
    }
}