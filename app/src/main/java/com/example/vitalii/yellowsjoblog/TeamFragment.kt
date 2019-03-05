package com.example.vitalii.yellowsjoblog


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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

    private val team:MutableList<Users> = ArrayList()
    private val adapter = TeamAdapter(team)
    private val connect = ServerConnection()

    private lateinit var mRecyclerView:RecyclerView

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)

        sp = this.activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        serverConnect()
        return view
    }

    private var responseSaveUsers:List<Users>? = null

    private fun serverConnect(){

        val token = sp.getString("token","")
        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        connect.createService(token!!).getUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                team.addAll(response.body()!!)
                adapter.updateRecycler(response.body()!!.toMutableList())
                if(response.body()!=null){
                    responseSaveUsers = response.body()!!
                    val nameOfUsers = ArrayList<String>()
                    for(user in response.body()!!){
                        if (user.fullName!= null){
                            nameOfUsers.add(user.fullName!!)
                        }
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
        })
    }
}


