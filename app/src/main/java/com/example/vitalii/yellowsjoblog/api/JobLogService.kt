package com.example.vitalii.yellowsjoblog.api

import retrofit2.Call
import retrofit2.http.*


interface JobLogService {
    @GET("/api/tasks/{user}")
    fun getDashboard(@Path("user") user:String):Call<List<Reports>>

    @GET("/api/tasks")
    fun getTasks(@QueryMap(encoded = false) filter:MutableMap<String,String>):Call<List<Reports>>

    @GET("/api/users")
    fun getUsers():Call<List<Users>>

    @GET("/api/projects")
    fun getProjects():Call<List<Projects>>

    @GET("/api/clients")
    fun getClients():Call<List<Clients>>

    @POST("api/user/token")
    fun basicLogin(): Call<Token>

    @POST("api/clients/new")
    fun newCLient(@Body client:AddClient):Call<String>

    @POST("api/projects/new")
    fun newProject(@Body project:AddProject):Call<String>
}