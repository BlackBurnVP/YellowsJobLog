package com.example.vitalii.yellowsjoblog


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.adapters.MultiSelectSpinner
import com.example.vitalii.yellowsjoblog.adapters.TeamAdapter
import com.example.vitalii.yellowsjoblog.api.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TeamFragment : Fragment() {

    private lateinit var mRecyclerView:RecyclerView

    val team:MutableList<Users> = ArrayList()
    val adapter = TeamAdapter(team)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team, container, false)

        mRecyclerView = view!!.findViewById(R.id.teamRecycler)
        val layoutManager = LinearLayoutManager(activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter
        serverConnect()
        return view
    }


    private var service:JobLogService? = null
    var retrofit:Retrofit? = null
    var usersObject:Callback<List<Users>>? = null
    var users:Call<List<Users>>? = null
    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"
    private var responseSaveUsers:List<Users>? = null

    fun serverConnect(){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        usersObject = object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                team.addAll(response.body()!!)
                adapter.updateRecycler(response.body()!!.toMutableList())
                if(response.body()!=null){
                    responseSaveUsers = response.body()!!
                    val nameOfUsers = ArrayList<String>()
                    for(user in response.body()!!){
                        nameOfUsers.add(user.fullName!!)
                    }
                    val usersID = ArrayList<Int>()
                    for (user in response.body()!!){
                        usersID.add(user.id!!)
                    }
                    val reports = MultiSelectSpinner(context!!)
                    reports.addUsers(response.body()!!)
                    //mUsersSpinner.setItems(nameOfUsers)
                    //ed.putStringSet("users",nameOfUsers.toMutableSet()).commit()
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }

        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit?.create(JobLogService::class.java)
        users = service?.getUsers()
        users?.enqueue(usersObject)
    }
}


