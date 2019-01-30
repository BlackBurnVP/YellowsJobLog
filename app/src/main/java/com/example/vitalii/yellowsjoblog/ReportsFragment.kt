package com.example.vitalii.yellowsjoblog

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.adapters.ReportsAdapter
import com.example.vitalii.yellowsjoblog.api.ReportsPOKO
import com.example.vitalii.yellowsjoblog.api.JobLogService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.vitalii.yellowsjoblog.adapters.MultiSelectSpinner
import com.example.vitalii.yellowsjoblog.api.ProjectsPOKO
import com.example.vitalii.yellowsjoblog.api.Users
import java.util.*
import android.text.format.DateUtils
import android.view.*
import kotlin.collections.ArrayList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.text.SimpleDateFormat

class ReportsFragment : Fragment() {

    private lateinit var mRecycleView:RecyclerView
    lateinit var mUsersSpinner:MultiSelectSpinner
    private lateinit var mProjectsSpinner:MultiSelectSpinner
    private lateinit var mButtonStartDate: Button
    private lateinit var mButtonEndDate:Button
    private lateinit var mButton: Button

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    private var tasks:MutableList<ReportsPOKO> = ArrayList()

    private var service:JobLogService? = null
    private var retrofit:Retrofit? = null

    private var responseSaveReports:List<ReportsPOKO>? = null
    private var responseSaveUsers:List<Users>? = null
    private var responseSaveProjects:List<ProjectsPOKO>? = null

    private var filter = mutableMapOf<String,String>()
    private var adapter = ReportsAdapter(tasks)
    private var startDate:Long? = null
    private var endDate:Long? = null
    private var date:String = ""

    private val dateAndTime = Calendar.getInstance()
    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"
    private val BASE_URL = "http://joblog.yellows.pl/"

    private val strDateFormat = "yyyy-MM-dd"
    private val dateFormat = SimpleDateFormat(strDateFormat)

    private var saveUsersList:MutableSet<String>? = null
    private var saveProjectsList:MutableSet<String>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        setHasOptionsMenu(true)

        sp = this.activity!!.getSharedPreferences("TIMER", Context.MODE_PRIVATE)
        ed = sp.edit()

        saveUsersList = sp.getStringSet("users", mutableSetOf())
        saveProjectsList = sp.getStringSet("projects", mutableSetOf())


        mButtonStartDate = view!!.findViewById(R.id.btnStartDate)
        mButtonStartDate.setOnClickListener(setDate)
        mButtonEndDate = view!!.findViewById(R.id.btnEndDate)
        mButtonEndDate.setOnClickListener(setDate)

        mRecycleView = view!!.findViewById(R.id.recycler_reports)
        val itemDecor = DividerItemDecoration(context,0)
        mRecycleView.addItemDecoration(itemDecor)

        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecycleView.layoutManager = layoutManager
        mRecycleView.adapter = adapter

        mUsersSpinner = MultiSelectSpinner(activity!!)
        mUsersSpinner.setUsers(saveUsersList!!.toList())
        //mUsersSpinner.setSelection(0)

        mProjectsSpinner = MultiSelectSpinner(activity!!)
        mProjectsSpinner.setProjects(saveProjectsList!!.toList())
        //mProjectsSpinner.setSelection(0)

        endDate = dateAndTime.timeInMillis
        setInitialDateTime(mButtonEndDate,dateFormat.format(endDate))
        dateAndTime.add(Calendar.DATE,-7)
        startDate = dateAndTime.timeInMillis
        setInitialDateTime(mButtonStartDate,dateFormat.format(startDate))
        date = "${mButtonStartDate.text}+${mButtonEndDate.text}"

        filter = mutableMapOf("date" to date)
        serverConnect(filter)

        return view
    }

    /**
     * Method for Filters
     */
    private fun applyFilters(){
        println("usersListID ${mUsersSpinner.usersListOfID}")
        println("projectsListID ${mProjectsSpinner.projectsListOfID}")
        date = "${mButtonStartDate.text}+${mButtonEndDate.text}"
        filter = mutableMapOf("date" to date)
        if(mUsersSpinner.usersListOfID.isNotEmpty()){
            val sb = StringBuilder()
            for (i in mUsersSpinner.usersListOfID.indices){
                val num = mUsersSpinner.usersListOfID[i]
                sb.append(num)
                sb.append("+")
            }
            val usersStringID = sb.toString()
            filter.put("users",usersStringID)
        }
        if(mProjectsSpinner.projectsListOfID.isNotEmpty()){
            val sb = StringBuilder()
            for (i in mProjectsSpinner.projectsListOfID.indices){
                val num = mProjectsSpinner.projectsListOfID[i]
                sb.append(num)
                sb.append("+")
            }
            val projectsStringID = sb.toString()
            filter.put("projects",projectsStringID)
        }
        serverConnect(filter)
    }

    /**
     * Method for DataPicker
     */

    private val setDate= View.OnClickListener { btn ->
        val currentDate = Date()
        val date = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateAndTime.set(Calendar.YEAR, year)
            dateAndTime.set(Calendar.MONTH, month)
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if(btn.id == R.id.btnStartDate){
                startDate = dateAndTime.timeInMillis
                val startDateString = dateFormat.format(startDate)
                println(startDate!!)
                setInitialDateTime(btn, startDateString!!)
            };else{
                endDate = dateAndTime.timeInMillis
                val endDateString = dateFormat.format(endDate)
                if (endDate!!<startDate!!){
                    Toast.makeText(activity!!,"You have to change end Date",Toast.LENGTH_SHORT).show()
                    println("ERROR")
                };else if (endDate!!>currentDate.time){
                    Toast.makeText(activity!!,"You have to change end Date",Toast.LENGTH_SHORT).show()
                };else{
                    setInitialDateTime(btn,endDateString!!)
                }
            } },
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH))
            .show()
    }

    /**
     * Setting buttons text to picked time
     * @param button
     * @param time in Long from DataPicker
     */

    private fun setInitialDateTime(button:View,time:Long) {
        button as Button
            button.text = DateUtils.formatDateTime(activity, time,
                DateUtils.FORMAT_NUMERIC_DATE or
                        DateUtils.FORMAT_SHOW_YEAR
            )
    }

    /**
     * Setting buttons text to picked time
     * @param button
     * @param time in String from DataPicker
     */

    private fun setInitialDateTime(button:View,time:String) {
        button as Button
        button.text = time
    }

    /**
     * Setting menu at toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
        inflater?.inflate(R.menu.filter,menu)
    }
    /**
     * Setting OnClickListeners for toolbar menu items
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.usersFilter -> mUsersSpinner.performClick()
            R.id.projectsFilter -> mProjectsSpinner.performClick()
            R.id.filter -> applyFilters()
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * Connection to server
     * @param filter of Map defined for input filters for tasks
     */
    private var reportsObject:Callback<List<ReportsPOKO>>? = null
    private var reports:Call<List<ReportsPOKO>>? = null
    private var usersObject:Callback<List<Users>>? = null
    private var users:Call<List<Users>>? = null
    private var projectsObject:Callback<List<ProjectsPOKO>>? = null
    private var projects:Call<List<ProjectsPOKO>>? = null

    private fun serverConnect(filter:MutableMap<String,String>){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        reportsObject = object : Callback<List<ReportsPOKO>> {
            override fun onResponse(call: Call<List<ReportsPOKO>>, response: Response<List<ReportsPOKO>>) {
                if (response.body() != null) {
                    responseSaveReports = response.body()!!
                    tasks.addAll(response.body()!!)
                    adapter.updateRecycler(response.body()!!.toMutableList())
                    //println("Tasks: $tasks")
                }; else {
                    Toast.makeText(activity!!, "Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReportsPOKO>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        }

        usersObject = object :Callback<List<Users>>{
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if(response.body()!=null){
                    responseSaveUsers = response.body()!!
                    val nameOfUsers = ArrayList<String>()
                    for(user in response.body()!!){
                        nameOfUsers.add(user.fullName!!)
                    }
                    mUsersSpinner.addUsers(response.body()!!)
                    //mUsersSpinner.setItems(nameOfUsers)
                    ed.putStringSet("users",nameOfUsers.toMutableSet()).commit()
                };else{
                    Toast.makeText(activity!!,"Server answer is null",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        }

        projectsObject = object :Callback<List<ProjectsPOKO>>{
            override fun onResponse(call: Call<List<ProjectsPOKO>>, response: Response<List<ProjectsPOKO>>) {
                if(response.body()!=null) {
                    responseSaveProjects = response.body()!!
                    val nameOfProjects = ArrayList<String>()
                    for (project in response.body()!!) {
                        nameOfProjects.add(project.name!!)
                    }
                    println("projects response - ${response.body()!!}")
                    mProjectsSpinner.addProjects(response.body()!!)
                    ed.putStringSet("projects",nameOfProjects.toMutableSet()).commit()
                };else{
                    Toast.makeText(activity!!,"Server answer is null",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ProjectsPOKO>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit?.create(JobLogService::class.java)
        reports = service?.getTasks(filter)
        reports?.enqueue(reportsObject)
        if(responseSaveUsers==null) {
            users = service?.getUsers()
            users?.enqueue(usersObject)
        }
        if(responseSaveProjects==null){
            projects = service?.getProjects()
            projects?.enqueue(projectsObject)
        }
    }

    override fun onResume() {
        super.onResume()
        sp = this.activity!!.getSharedPreferences("TIMER", Context.MODE_PRIVATE)
    }
}
