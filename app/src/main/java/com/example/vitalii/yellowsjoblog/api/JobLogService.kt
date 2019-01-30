package com.example.vitalii.yellowsjoblog.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface JobLogService {
    @GET("/api/tasks/{user}")
    fun getDashboard(@Path("user") user:String):Call<List<ReportsPOKO>>

    @GET("/api/tasks")
    fun getTasks(@QueryMap(encoded = false) filter:MutableMap<String,String>):Call<List<ReportsPOKO>>

    @GET("/api/users")
    fun getUsers():Call<List<Users>>

    @GET("/api/projects")
    fun getProjects():Call<List<ProjectsPOKO>>

    @GET("/api/clients")
    fun getClients():Call<List<ClientsPOKO>>
}