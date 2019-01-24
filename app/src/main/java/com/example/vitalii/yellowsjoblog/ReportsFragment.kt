package com.example.vitalii.yellowsjoblog

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.adapters.ReportsAdapter
import com.example.vitalii.yellowsjoblog.api.JobLogPOJO
import com.example.vitalii.yellowsjoblog.api.JobLogService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Spinner
import com.example.vitalii.yellowsjoblog.adapters.MultiSelectSpinner
import com.example.vitalii.yellowsjoblog.adapters.Projects
import com.example.vitalii.yellowsjoblog.adapters.Users


class ReportsFragment : Fragment() {

    private lateinit var mRecycleView:RecyclerView
    private lateinit var mUsersSpinner:MultiSelectSpinner
    private lateinit var mProjectsSpinner:MultiSelectSpinner
    private var tasks:MutableList<JobLogPOJO> = ArrayList()

    private val usersList = ArrayList<Users>()
    private val projectsList = ArrayList<Projects>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports, container, false)

        mRecycleView = view!!.findViewById(R.id.recycler_reports)
        val itemDecor:DividerItemDecoration = DividerItemDecoration(context,0)
        mRecycleView.addItemDecoration(itemDecor)

        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecycleView.layoutManager = layoutManager
        val adapter = ReportsAdapter(tasks)
        mRecycleView.adapter = adapter

        mUsersSpinner = view!!.findViewById(R.id.usersSpinner) as MultiSelectSpinner
        mUsersSpinner.setTitle("Users")
        usersList.add(Users(1,"Yaroslav Chebukin"))
        usersList.add(Users(2,"Vitalii Pshenychniuk"))
        mUsersSpinner.addUsers(usersList)
        mUsersSpinner.setSelection(0)


        mProjectsSpinner = view!!.findViewById(R.id.projectsSpinner) as MultiSelectSpinner
        mProjectsSpinner.setTitle("Projects")
        for (item in 1..5){
            projectsList.add(Projects(item,"Projects no $item"))
        }
        mProjectsSpinner.addProjects(projectsList)
        mProjectsSpinner.setSelection(0)

        val service = Retrofit.Builder()
            .baseUrl("http://joblog.yellows.pl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JobLogService::class.java)
        val selectedUser:String
        if (mUsersSpinner.selectedStrings.isNotEmpty()){
             selectedUser = mUsersSpinner.selectedStrings[0]
        };else{
            selectedUser = "Yaroslav Chebukin"
        }
            service.getTasks(selectedUser)
                .enqueue(object : Callback<List<JobLogPOJO>> {
                    override fun onResponse(call: Call<List<JobLogPOJO>>, response: Response<List<JobLogPOJO>>) {
                        if (response.body() != null) {
                            tasks.addAll(response.body()!!)
                            mRecycleView.adapter?.notifyDataSetChanged()
                            response.body()?.forEach { println(it) }
                        }; else {
                            Toast.makeText(activity!!, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<JobLogPOJO>>, t: Throwable) {
                        Toast.makeText(activity!!, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show()
                    }
                })
        return view
    }
}
