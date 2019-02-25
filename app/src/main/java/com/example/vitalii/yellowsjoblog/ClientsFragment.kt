package com.example.vitalii.yellowsjoblog


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.vitalii.yellowsjoblog.adapters.ClientsAdapter
import com.example.vitalii.yellowsjoblog.api.Clients
import com.example.vitalii.yellowsjoblog.api.AddClient
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private var client:MutableList<Clients> = ArrayList()
    private val adapter = ClientsAdapter(client)
    private val connect = ServerConnection()
    private var token =""

    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recycler, container, false)
        setHasOptionsMenu(true)

        sp = context!!.getSharedPreferences("PREF_NAME",Context.MODE_PRIVATE)
        ed = sp.edit()

        mRecyclerView = view!!.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        serverConnect()

        token = sp.getString("token","")

        return view
    }

    private fun serverConnect(){

        val token = sp.getString("token","")
        connect.createService(token!!).getClients().enqueue(object : Callback<List<Clients>> {
            override fun onResponse(call: Call<List<Clients>>, response: Response<List<Clients>>) {
                if(response.body()!=null) {
                    adapter.updateRecycler(response.body()!!.toMutableList())
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Clients>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu,inflater)
        inflater?.inflate(R.menu.add_new,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        view!!.findNavController().navigate(R.id.action_clientsFragment_to_addClientFragment)
        return super.onOptionsItemSelected(item)
    }
}
