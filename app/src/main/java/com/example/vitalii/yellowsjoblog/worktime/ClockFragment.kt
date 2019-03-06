package com.example.vitalii.yellowsjoblog.worktime


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vitalii.yellowsjoblog.LoginActivity
import com.example.vitalii.yellowsjoblog.MainActivity
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
@SuppressLint("SimpleDateFormat")
class ClockFragment : Fragment(){

    private lateinit var btnStartEnd:Button
    private lateinit var txtClock:TextView
    private val mHandler:Handler = Handler()
    private var mRunnable:Runnable = Runnable {}
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor
    private lateinit var mProjectSpinner: Spinner
    lateinit var edWorkDesc:EditText
    private lateinit var data:Date
    private var seconds:Long = 0
    private var startRun = false
    private var listOfProjects = ArrayList<String>()
    private val connect = ServerConnection()
    private var token:String = ""

    var isStopped = false

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        sp = context!!.getSharedPreferences("PREF_NAME",Context.MODE_PRIVATE)
        ed = sp.edit()

        ed.putLong("SECONDS",0).apply()
        token = sp.getString("token","")!!

        btnStartEnd = view!!.findViewById(R.id.btnStartEnd)
        txtClock = view.findViewById(R.id.txt_clock)
        mProjectSpinner = view.findViewById(R.id.project_spinner)
        edWorkDesc = view.findViewById(R.id.txt_work)

        edWorkDesc.setText(sp.getString("taskName",""))

        getProjects(token)

        MainActivity().loseFocus()

        onTimerStop()
        onTimerStart()
        btnStartEnd.setOnClickListener(onClick)
        return view
    }

    /**
     * Start/Stop timer button click
     */
    val onClick = View.OnClickListener { view ->
        val taskName = edWorkDesc.text.toString()
        val project = mProjectSpinner.selectedItem.toString()
        if(!startRun){
            // Start timer
            addTask(token,taskName,project)
            btnStartEnd.isEnabled = false
        };else{
            //Stop timer
            btnStartEnd.setOnLongClickListener {
                btnStartEnd.isEnabled = false
                endTask(token,taskName,project)
               true
            }
        }
    }

    /**
     * Starting Work Time Timer
     */
    private fun onTimerStart(){
        ed.putBoolean("thread", true).apply()
        mRunnable = Runnable {
            seconds = sp.getLong("SECONDS",0)
            try {
                if (seconds!=0L){
                    startRun = true
                    btnStartEnd.text = getString(R.string.stop)
                }
            }catch (ex:Exception){}
            val hours = seconds/3600
            val minutes = (seconds%3600)/60
            val secs = (seconds%60)
            val time = String.format("%d:%02d:%02d", hours, minutes, secs)
            txtClock.text = time
            if (startRun){
                seconds++
                ed.putLong("SECONDS",seconds).apply()
            }
            if(!isStopped) mHandler.postDelayed(mRunnable,1000)
        }
        mHandler.postDelayed(mRunnable,1000)
    }
    /**
     * Stops Work Time Timer
     */

    private fun onTimerStop(){
        mHandler.removeCallbacks(mRunnable)
    }

    /**
     * Add new task
     * @param token Bearer token
     * @param taskName String description of work
     * @param project String name of project
     */
    private fun addTask(token: String, taskName:String,project:String){
        val user = sp.getString("currentUser","")
        connect.createService(token).newTask(AddTask(taskName,project,user!!)).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(context!!,"Task added",Toast.LENGTH_SHORT).show()
                btnStartEnd.isEnabled = true
                startRun = true
                btnStartEnd.text = getString(R.string.stop)
                connect.createService(token).getDashboard(user).clone()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context!!,"Server not responding. Please, try again later",Toast.LENGTH_SHORT).show()
                btnStartEnd.isEnabled = true
            }
        })
    }
    /**
     * End current task
     */
    private fun endTask(token: String, taskName:String, project:String){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date()
        val endDate = dateFormat.format(date)
        val user = sp.getString("currentUser","")
        connect.createService(token).endTask(EndTask(taskName,endDate,project,user!!)).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Toast.makeText(context!!,"Task ended",Toast.LENGTH_SHORT).show()
                btnStartEnd.isEnabled = true
                startRun = false
                data = Date()
                seconds = 0L
                ed.putLong("SECONDS",0L).apply()
                btnStartEnd.text = getString(R.string.start)
                connect.createService(token).getDashboard(user).clone()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context!!,"Server not responding. Please, try again later",Toast.LENGTH_SHORT).show()
                btnStartEnd.isEnabled = true
            }
        })
    }

    /**
     * Get list of projects from the server
     */
    private fun getProjects(token: String){
        connect.createService(token).getProjects()
           .enqueue(object : Callback<List<Projects>> {
            override fun onResponse(call: Call<List<Projects>>, response: Response<List<Projects>>) {
                if(response.body()!=null) {
                    for (project in response.body()!!) {
                        listOfProjects.add(project.name!!)
                        fillSpinner(listOfProjects)
                    }
                };else{
                    val intent = Intent(context!!,LoginActivity::class.java)
                    startActivity(intent)
                    ed.putBoolean("logged",false).apply()
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Projects>>, t: Throwable) {
                Toast.makeText(activity!!, "Server not responding!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fillSpinner(list:ArrayList<String>){
        val adapter = ArrayAdapter(activity!!,android.R.layout.simple_spinner_item,list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mProjectSpinner.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
        isStopped = true
    }

    override fun onResume() {
        super.onResume()
        isStopped = false
    }
}
