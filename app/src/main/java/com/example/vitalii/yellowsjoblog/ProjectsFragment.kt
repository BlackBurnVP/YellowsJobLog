package com.example.vitalii.yellowsjoblog


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.adapters.ProjectsAdapter
import com.example.vitalii.yellowsjoblog.api.JobLogService
import com.example.vitalii.yellowsjoblog.api.ProjectsPOKO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private val project:MutableList<ProjectsPOKO> = ArrayList()
    private val adapter = ProjectsAdapter(project)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)

        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        serverConnect()

        return view
    }

    private var projectsObject:Callback<List<ProjectsPOKO>>? = null
    private var projects:Call<List<ProjectsPOKO>>? = null
    private var responseSaveProjects:List<ProjectsPOKO>? = null
    private lateinit var retrofit:Retrofit
    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"
    private lateinit var service:JobLogService

    private fun serverConnect(){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        projectsObject = object : Callback<List<ProjectsPOKO>> {
            override fun onResponse(call: Call<List<ProjectsPOKO>>, response: Response<List<ProjectsPOKO>>) {
                if(response.body()!=null) {
                    responseSaveProjects = response.body()!!
                    adapter.updateRecycler(response.body()!!.toMutableList())
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProjectsPOKO>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit?.create(JobLogService::class.java)
        if(responseSaveProjects==null){
            projects = service?.getProjects()
            projects?.enqueue(projectsObject)
        }
    }

}
