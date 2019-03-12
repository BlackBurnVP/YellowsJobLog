package com.example.vitalii.yellowsjoblog.worktime

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
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.adapters.ClickListener
import com.example.vitalii.yellowsjoblog.adapters.DashboardAdapter
import com.example.vitalii.yellowsjoblog.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class StatsFragment : Fragment(){

    private lateinit var mRecyclerView:RecyclerView
    private var stats:MutableList<Reports> = ArrayList()
    private val adapter = DashboardAdapter(stats)
    private val connect = ServerConnection()
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)

        sp = context!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        getTasks()
        return view
    }

    private fun recyclerClick(){
        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        mRecyclerView.addOnItemTouchListener(ClickListener(activity!!, mRecyclerView, object : ClickListener.OnItemClickListener {
            override fun onLongItemClick(v: View?, position: Int) {
            }

            override fun onItemClick(view: View, position: Int) {
                val id = stats[position].id
                val desc = stats[position].name
                val project = stats[position].projectName
                val dateStart = stats[position].dateStart
                val dateEnd = stats[position].dateEnd
                val timeStart = stats[position].hourStart
                val timeEnd = stats[position].hourEnd

                val args = Bundle()
                args.putString("idTask",id.toString())
                args.putString("name",desc)
                args.putString("project",project)
                args.putString("startDate",dateStart)
                args.putString("endDate",dateEnd)
                args.putString("startTime",timeStart)
                args.putString("endTime",timeEnd)
                args.putInt("position",position)
                view.findNavController().navigate(R.id.action_global_updateTaskFragment,args)
            }
        }))
    }

     private fun getTasks(){

        val token = sp.getString("token","")
        val user = sp.getString("currentUser","")

        connect.createService(token!!).getDashboard(user!!).enqueue(object : Callback<List<Reports>> {
            override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                val workDesc = activity!!.findViewById<EditText>(R.id.txt_work)
                try {
                    adapter.updateRecycler(response.body()!!.toMutableList())
                    recyclerClick()
                    val currentTime = dateFormat.parse(dateFormat.format(Date())).time
                    var startTime = ""
                    for (report in response.body()!!){
                        if (report.hourEnd == null){
                            startTime = report.hourStart!!
                            if (report.name!=null) {
                                workDesc.setText(report.name!!)
                            }else{
                                workDesc.setText("No description")
                            }
                            ed.putString("currentProject",report.projectName).apply()
                            break
                        }
                    }
                    val tml = dateFormat.parse(startTime).time
                    val timer = (currentTime-tml)/1000
                    ed.putLong("SECONDS",timer).apply()
                }catch (ex:Exception){}
            }
            override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                Toast.makeText(activity!!, "Server not responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
