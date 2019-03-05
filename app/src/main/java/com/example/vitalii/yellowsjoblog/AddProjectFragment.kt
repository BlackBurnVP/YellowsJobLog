package com.example.vitalii.yellowsjoblog

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vitalii.yellowsjoblog.adapters.MultiSelectSpinner
import com.example.vitalii.yellowsjoblog.api.AddProject
import com.example.vitalii.yellowsjoblog.api.Clients
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import com.example.vitalii.yellowsjoblog.api.Users
import kotlinx.android.synthetic.main.fragment_add_project.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.AdapterView
import androidx.navigation.findNavController


class AddProjectFragment : Fragment() {

    private lateinit var mProjectName:EditText
    private lateinit var mClientsSpinner:Spinner
    private lateinit var mUsersSpinners:MultiSelectSpinner
    private lateinit var selectedClient:String

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_project, container, false)
        sp = this.activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        users()
        clients()

        mProjectName = view!!.findViewById(R.id.edProjectName)

        mClientsSpinner = view.findViewById(R.id.clientsSpinner)
        mUsersSpinners = view.findViewById(R.id.usersSpinner)
        mUsersSpinners.setTitle("Choose users")

        val btn:Button = view.findViewById(R.id.btnAddProject)
        btn.setOnClickListener {
            val projectName = mProjectName.text.toString()
            val usersArray = ArrayList<String>()
            selectedClient = mClientsSpinner.selectedItem.toString()
            for (i in mUsersSpinners.selectedUsers){
                usersArray.add(i)
            }
            addProject(projectName, selectedClient, usersArray)
        }

//        mClientsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(activity!!,nameOfClients[position],Toast.LENGTH_SHORT).show()
//                selectedClient = nameOfClients[position]
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }
        // Inflate the layout for this fragment
        return view

    }

    private val connect = ServerConnection()

    private fun users(){
        val token = sp.getString("token","")
        connect.createService(token!!).getUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if(response.body()!=null){
                    val nameOfUsers = ArrayList<String>()
                    for (user in response.body()!!) {
                        if (user.fullName!= null){
                            nameOfUsers.add(user.fullName!!)
                        }
                    }
                    mUsersSpinners.addUsers(response.body()!!)
                    ed.putStringSet("users",nameOfUsers.toMutableSet()).commit()
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private var nameOfClients = ArrayList<String>()

    private fun clients(){
        val token = sp.getString("token","")
        connect.createService(token!!).getClients().enqueue(object : Callback<List<Clients>> {
            override fun onResponse(call: Call<List<Clients>>, response: Response<List<Clients>>) {
                if(response.body()!=null){
                    for(client in response.body()!!){
                        nameOfClients.add(client.name!!)
                        fillSpinner(nameOfClients)
                    }
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Clients>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addProject(name:String, client:String?, users:ArrayList<String>){
        val token = sp.getString("token","")
        when {
            name=="" -> mProjectName.error = "It's required field"
            client == null -> Toast("You have to choose client")
            users.isEmpty() -> Toast("You have to choose users")
            else -> connect.createService(token!!).newProject(AddProject(name, client, users))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.body() != null) {
                            Toast.makeText(activity!!, "OK", Toast.LENGTH_SHORT).show()
                            view!!.findNavController().navigate(R.id.action_addProjectFragment_to_projectsFragment)
                        } else {
                           Toast("ANSWER IS NULL")
                        }
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                       Toast("Something went wrong")
                    }
                })
        }
    }

    private fun fillSpinner(list:ArrayList<String>){
        val adapter = ArrayAdapter<String>(activity!!,android.R.layout.simple_spinner_item,list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mClientsSpinner.adapter = adapter
    }

    private fun Toast(msg:String){
        Toast.makeText(context!!,msg,Toast.LENGTH_SHORT).show()
    }
}
