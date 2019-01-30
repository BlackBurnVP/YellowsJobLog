package com.example.vitalii.yellowsjoblog.worktime

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.adapters.StatsAdapter
import com.example.vitalii.yellowsjoblog.api.JobLogService
import com.example.vitalii.yellowsjoblog.api.ReportsPOKO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StatsFragment : Fragment() {

    private lateinit var mRecyclerView:RecyclerView
    private var service: JobLogService? = null
    private var retrofit:Retrofit? = null
    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"
    private var stats:MutableList<ReportsPOKO> = ArrayList()
    private val adapter = StatsAdapter(stats)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        mRecyclerView = view!!.findViewById(R.id.dashboardRecycler)
        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter
        getTasks()
        return view
    }

    private var dashboardObject:Callback<List<ReportsPOKO>>? = null
    private var reports:Call<List<ReportsPOKO>>? = null

    private fun getTasks(){

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        dashboardObject = object : Callback<List<ReportsPOKO>>{
            override fun onResponse(call: Call<List<ReportsPOKO>>, response: Response<List<ReportsPOKO>>) {
                adapter.updateRecycler(response.body()!!.toMutableList())
            }

            override fun onFailure(call: Call<List<ReportsPOKO>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }

        }
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit?.create(JobLogService::class.java)
        reports = service?.getDashboard("user")
        reports?.enqueue(dashboardObject)
    }
}
