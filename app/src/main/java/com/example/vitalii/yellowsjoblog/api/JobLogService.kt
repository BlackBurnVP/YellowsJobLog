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

    @POST("api/tasks/update")
    fun endTask(@Body task:EndTask):Call<String>

    @POST("api/tasks/new")
    fun newTask(@Body task:AddTask):Call<String>

    @POST("api/tasks/{id}/edit")
    fun editTask(@Path("id") id:String, @Body task:UpdateTask):Call<String>
}