package com.example.vitalii.yellowsjoblog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import android.text.format.DateUtils
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.*
import androidx.navigation.findNavController
import com.example.vitalii.yellowsjoblog.api.Projects
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import com.example.vitalii.yellowsjoblog.api.UpdateTask
import com.example.vitalii.yellowsjoblog.worktime.StatsFragment
import kotlinx.android.synthetic.main.fragment_update_task.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class UpdateTaskFragment : Fragment() {

    private var dateAndTime = Calendar.getInstance()
    private lateinit var mStartDate:Button
    private lateinit var mEndDate:Button
    private lateinit var mWorkDescription:EditText
    private lateinit var mProjectSpinner:Spinner
    private lateinit var mUpdate:Button
    private val connect = ServerConnection()

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor


    @SuppressLint("CommitPrefEdits", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_update_task, container, false)

        sp = context!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        val token = sp.getString("token","")

        mStartDate = view.findViewById(R.id.btnStartDate)
        mEndDate = view.findViewById(R.id.btnEndDate)
        mWorkDescription = view.findViewById(R.id.edWorkDesc)
        mProjectSpinner = view.findViewById(R.id.spinnerProjects)
        mUpdate = view.findViewById(R.id.btnUpdate)
        getProjects(token!!)

        mWorkDescription.setText(arguments?.getString("name"))
            mStartDate.text = "${arguments?.getString("startDate")} ${arguments?.getString("startTime")}"
            mEndDate.text = "${arguments?.getString("endDate")} ${arguments?.getString("endTime")}"
        mStartDate.setOnClickListener(setDate)
        mEndDate.setOnClickListener(setDate)

        mUpdate.setOnClickListener{
            val id = arguments?.getString("idTask")
            val name = edWorkDesc.text.toString()
            val project = mProjectSpinner.selectedItem.toString()
            val dateStart = mStartDate.text.toString()
            val dateEnd = mEndDate.text.toString()

            updateTask(token,id!!,name,project,dateStart,dateEnd)
        }

        return view
    }

    private val setDate = View.OnClickListener {
        val dr:String? = if (it == mStartDate){
            arguments?.getString("startDate")
        }else{
            arguments?.getString("endDate")
        }

        val temp = dr!!.split("-")
        val yy = temp[0].toInt()
        val mm = temp[1].toInt()
        val dd = temp[2].toInt()
        println(temp[1].toInt())
        println("CALENDAR ${dateAndTime.get(Calendar.MONTH)}")
        val date = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime.set(Calendar.YEAR, year)
            dateAndTime.set(Calendar.MONTH, monthOfYear)
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setTime.onClick(it)
            setInitialDateTime(it,dateAndTime.timeInMillis)
        },
            yy-4,
            mm-1,
            dd
        ).show()
    }

    private val setTime = View.OnClickListener {
        val tm:String
        if (it == mStartDate){
            tm = arguments?.getString("startTime")!!
        }else{
            tm = arguments?.getString("endTime")!!
        }
        val temp = tm.split(":")
        TimePickerDialog(
            activity!!, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                dateAndTime.set(Calendar.MINUTE, minute)
                setInitialDateTime(it,dateAndTime.timeInMillis)
            },
            temp[0].toInt(),
            temp[1].toInt(),true
        ).show()
    }

    private fun setInitialDateTime(btn:View,time:Long) {
        btn as Button
        btn.text = changeDataFormat(time)
    }


    private fun changeDataFormat(strDate:String):String{
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val date = dateFormat.parse(strDate)
        return dateFormat.format(date)
    }
    private fun changeDataFormat(time:Long):String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return dateFormat.format(time)
    }

    private fun updateTask(token:String, id:String, name:String, project:String, dateStart:String, dateEnd:String){
        connect.createService(token).editTask(id,UpdateTask(name,project,dateStart,dateEnd)).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body()!=null){
                    view!!.findNavController().navigate(R.id.action_updateTaskFragment_to_statsFragment)
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context!!,"ERROR",Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun getProjects(token:String){
        connect.createService(token).getProjects().clone().enqueue(object:Callback<List<Projects>>{
            override fun onResponse(call: Call<List<Projects>>, response: Response<List<Projects>>) {
                if(response.body()!=null) {
                    val listOfProjects = ArrayList<String>()
                    for (project in response.body()!!) {
                        listOfProjects.add(project.name!!)
                        fillSpinner(listOfProjects)
                    }
                }
            }
            override fun onFailure(call: Call<List<Projects>>, t: Throwable) {}
        })
    }
    private fun fillSpinner(list:ArrayList<String>){
        try {
            val adapter = ArrayAdapter(activity!!,android.R.layout.simple_spinner_item,list)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mProjectSpinner.adapter = adapter
            mProjectSpinner.setSelection(arguments?.getInt("position")!!)
        }catch (ex:Exception){}
    }
}
