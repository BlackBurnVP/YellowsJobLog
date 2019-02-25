package com.example.vitalii.yellowsjoblog.worktime

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
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.adapters.DashboardAdapter
import com.example.vitalii.yellowsjoblog.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class StatsFragment : Fragment() {

    private lateinit var mRecyclerView:RecyclerView
    private var stats:MutableList<Reports> = ArrayList()
    private val adapter = DashboardAdapter(stats)
    private val connect = ServerConnection()

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)

        sp = context!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        //getTasks()
        return view
    }

    override fun onStart() {
        getTasks()
        super.onStart()
    }

    private fun getTasks(){

        val token = sp.getString("token","")
        val user = sp.getString("currentUser","")

        connect.createService(token!!).getDashboard(user!!).enqueue(object : Callback<List<Reports>> {
            override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                try {
                    adapter.updateRecycler(response.body()!!.toMutableList())
                }catch (ex:Exception){}

            }
            override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
