package com.example.vitalii.yellowsjoblog.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface JobLogService {
    @GET("/api/tasksapi/{user}")
    fun getTasks(@Path("user") user:String): Call<List<JobLogPOJO>>
//    fun getTasks(@QueryMap filters:Map<String,ArrayList<Int>>):Call<List<JobLogPOJO>>
}