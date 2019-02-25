package com.example.vitalii.yellowsjoblog

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.vitalii.yellowsjoblog.api.AddClient
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddClientFragment : Fragment() {

    private var token = ""
    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor
    private val connect = ServerConnection()

    private lateinit var mClientName:EditText
    private lateinit var mClientColor:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_client, container, false)
        sp = context!!.getSharedPreferences("PREF_NAME",Context.MODE_PRIVATE)
        ed = sp.edit()

        token = sp.getString("token","")!!

        mClientName = view.findViewById(R.id.edClientName)
        mClientColor = view.findViewById(R.id.edClientColor)

        // Inflate the layout for this fragment
        val btn:Button = view.findViewById(R.id.btnAddClient)
        btn.setOnClickListener {
            val clientName = mClientName.text.toString()
            val clientColor = mClientColor.text.toString()
            addClient(clientName,clientColor)
        }
        return view
    }

    private fun addClient(name:String,color:String){
        connect.createService(token).newCLient(AddClient(name,color)).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body()!= null){
                    Toast.makeText(context!!,"SUCCESS", Toast.LENGTH_SHORT).show()
                    view!!.findNavController().navigate(R.id.action_addClientFragment_to_clientsFragment)
                }else{
                    Toast.makeText(context!!,"NULL BODY", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context!!,"FAILED", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
