package com.example.vitalii.yellowsjoblog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.adapters.ReportsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.vitalii.yellowsjoblog.adapters.MultiSelectSpinner
import java.util.*
import android.text.format.DateUtils
import android.view.*
import com.example.vitalii.yellowsjoblog.api.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat

class ReportsFragment : Fragment() {

    @SuppressLint("SimpleDateFormat")

    private lateinit var mRecycleView: RecyclerView
    private lateinit var mUsersSpinner: MultiSelectSpinner
    private lateinit var mProjectsSpinner: MultiSelectSpinner
    private lateinit var mButtonStartDate: Button
    private lateinit var mButtonEndDate: Button

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    private var tasks: MutableList<Reports> = ArrayList()

    private var filter = mutableMapOf<String, String>()
    private var adapter = ReportsAdapter(tasks)
    private var startDate: Long? = null
    private var endDate: Long? = null
    private var date: String = ""

    private val dateAndTime = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var saveUsersList: MutableSet<String>? = null

    private var saveProjectsList: MutableSet<String>? = null

    private val connect = ServerConnection()
    private lateinit var token: String

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports, container, false)
        setHasOptionsMenu(true)

        sp = this.activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        token = sp.getString("token", "")!!

        saveUsersList = sp.getStringSet("users", mutableSetOf())
        saveProjectsList = sp.getStringSet("projects", mutableSetOf())

        mButtonStartDate = view!!.findViewById(R.id.btnStartDate)
        mButtonStartDate.setOnClickListener(setDate)
        mButtonEndDate = view.findViewById(R.id.btnEndDate)
        mButtonEndDate.setOnClickListener(setDate)

        mRecycleView = view.findViewById(R.id.recycler_reports)

        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecycleView.layoutManager = layoutManager
        mRecycleView.adapter = adapter

        mUsersSpinner = MultiSelectSpinner(activity!!)
        mUsersSpinner.setUsers(saveUsersList!!.toList())

        mProjectsSpinner = MultiSelectSpinner(activity!!)
        mProjectsSpinner.setProjects(saveProjectsList!!.toList())

        endDate = dateAndTime.timeInMillis
        setInitialDateTime(mButtonEndDate, dateFormat.format(endDate))
        dateAndTime.add(Calendar.DATE, -7)
        startDate = dateAndTime.timeInMillis
        setInitialDateTime(mButtonStartDate, dateFormat.format(startDate))
        date = "${mButtonStartDate.text}+${mButtonEndDate.text}"

        filter = mutableMapOf("date" to date)

        getProjects()
        getUsers()
        getReports(filter)

        return view
    }

    /**
     * Method for Filters
     */
    private fun applyFilters() {
        if (mUsersSpinner.usersListOfID.isNotEmpty()) {
            val sb = StringBuilder()
            for (i in mUsersSpinner.usersListOfID.indices) {
                val num = mUsersSpinner.usersListOfID[i]
                sb.append(num)
                sb.append("+")
            }
            val usersStringID = sb.toString()
            filter["users"] = usersStringID
        }
        if (mProjectsSpinner.projectsListOfID.isNotEmpty()) {
            val sb = StringBuilder()
            for (i in mProjectsSpinner.projectsListOfID.indices) {
                val num = mProjectsSpinner.projectsListOfID[i]
                sb.append(num)
                sb.append("+")
            }
            val projectsStringID = sb.toString()
            filter["projects"] = projectsStringID
        }
        getReports(filter)
    }

    /**
     * Method for DataPicker
     */

    private val setDate = View.OnClickListener { btn ->
        val currentDate = Date()
        val date = DatePickerDialog(
            activity!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateAndTime.set(Calendar.YEAR, year)
                dateAndTime.set(Calendar.MONTH, month)
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                if (btn.id == R.id.btnStartDate) {
                    startDate = dateAndTime.timeInMillis
                    val startDateString = dateFormat.format(startDate)
                    setInitialDateTime(btn, startDateString!!)
                    date = "$startDateString+${mButtonEndDate.text}"
                    filter["date"] = date
                    getReports(filter)
                }; else {
                    endDate = dateAndTime.timeInMillis
                    val endDateString = dateFormat.format(endDate)
                    when {
                        endDate!! < startDate!! -> {
                            Toast.makeText(activity!!, "You have to change end Date", Toast.LENGTH_SHORT).show()
                        }
                        endDate!! > currentDate.time -> Toast.makeText(
                            activity!!,
                            "You have to change end Date",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> {
                            setInitialDateTime(btn, endDateString!!)
                            date = "${mButtonStartDate.text}+$endDateString"
                            filter["date"] = date
                            getReports(filter)
                        }
                    }
                }
            },
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /**
     * Setting buttons text to picked time
     * @param button
     * @param time in Long from DataPicker
     */

    private fun setInitialDateTime(button: View, time: Long) {
        button as Button
        button.text = DateUtils.formatDateTime(
            activity, time,
            DateUtils.FORMAT_NUMERIC_DATE or
                    DateUtils.FORMAT_SHOW_YEAR
        )
    }

    /**
     * Setting buttons text to picked time
     * @param button
     * @param time in String from DataPicker
     */

    private fun setInitialDateTime(button: View, time: String) {
        button as Button
        button.text = time
    }

    /**
     * Setting menu at toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
        inflater?.inflate(R.menu.filter, menu)
    }

    /**
     * Setting OnClickListeners for toolbar menu items
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.usersFilter -> mUsersSpinner.performClick()
            R.id.projectsFilter -> mProjectsSpinner.performClick()
            R.id.filter -> applyFilters()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Getting reports from server
     * @param filter Map defined for input filters for reports
     */
    private fun getReports(filter: MutableMap<String, String>) {

        connect.createService(token).getTasks(filter)
            .enqueue(object : Callback<List<Reports>> {
                override fun onResponse(call: Call<List<Reports>>, response: Response<List<Reports>>) {
                    if (response.body() != null) {
                        tasks.addAll(response.body()!!)
                        adapter.updateRecycler(response.body()!!.toMutableList())
                    }; else {
                        Toast.makeText(activity!!, "Server answer is null", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Reports>>, t: Throwable) {
                    Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    /**
     * Getting users from server
     */
    private fun getUsers() {

        connect.createService(token).getUsers()
            .enqueue(object : Callback<List<Users>> {
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                    if (response.body() != null) {
                        val nameOfUsers = ArrayList<String>()
                        for (user in response.body()!!) {
                            if(user.fullName!= null){
                                nameOfUsers.add(user.fullName!!)
                            }
                        }
                        mUsersSpinner.addUsers(response.body()!!)
                        ed.putStringSet("users", nameOfUsers.toMutableSet()).commit()
                    }; else {
                        Toast.makeText(activity!!, "Server answer is null", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                    Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
                }
            })
    }



    /**
     * Getting projects from server
     */
    private fun getProjects() {
        connect.createService(token).getProjects()
            .enqueue(object : Callback<List<Projects>> {
                override fun onResponse(call: Call<List<Projects>>, response: Response<List<Projects>>) {
                    if (response.body() != null) {
                        val nameOfProjects = ArrayList<String>()
                        for (project in response.body()!!) {
                            nameOfProjects.add(project.name!!)
                        }
                        mProjectsSpinner.addProjects(response.body()!!)
                        ed.putStringSet("projects", nameOfProjects.toMutableSet()).commit()
                    }; else {
                        Toast.makeText(activity!!, "Server answer is null", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Projects>>, t: Throwable) {
                    Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        sp = this.activity!!.getSharedPreferences("TIMER", Context.MODE_PRIVATE)
    }
}
