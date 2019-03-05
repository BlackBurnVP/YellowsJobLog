package com.example.vitalii.yellowsjoblog


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.vitalii.yellowsjoblog.adapters.ProjectsAdapter
import com.example.vitalii.yellowsjoblog.api.Projects
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private val project:MutableList<Projects> = ArrayList()
    private val adapter = ProjectsAdapter(project)
    private val connect = ServerConnection()
    private var responseSaveProjects:List<Projects>? = null
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)
        setHasOptionsMenu(true)

        sp = context!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        getProjects()

        return view
    }

    private fun getProjects(){

        val token = sp.getString("token","")
        connect.createService(token!!).getProjects()
            .enqueue(object : Callback<List<Projects>> {
            override fun onResponse(call: Call<List<Projects>>, response: Response<List<Projects>>) {
                if(response.body()!=null) {
                    responseSaveProjects = response.body()!!
                    adapter.updateRecycler(response.body()!!.toMutableList())
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Projects>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.add_new,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        view!!.findNavController().navigate(R.id.action_projectsFragment_to_addProjectFragment)
        return super.onOptionsItemSelected(item)
    }
}
