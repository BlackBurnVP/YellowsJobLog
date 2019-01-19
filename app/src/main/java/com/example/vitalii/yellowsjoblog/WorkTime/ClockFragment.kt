package com.example.vitalii.yellowsjoblog.WorkTime


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.vitalii.yellowsjoblog.R
import kotlinx.android.synthetic.main.fragment_clock.*
import kotlinx.android.synthetic.main.fragment_clock.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class ClockFragment : Fragment(), AdapterView.OnItemSelectedListener{

    private lateinit var start:Button
    private lateinit var txtTime:TextView
    private val mHandler:Handler = Handler()
    private var mRunnable:Runnable = Runnable {  }
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor
    private lateinit var spinner: Spinner
    private lateinit var testRecycler:RecyclerView
    private lateinit var workDesc:EditText
    private val data = Date()
    val strDateFormat = "HH:mm:ss"
    val dateFormat = SimpleDateFormat(strDateFormat)
    private var startTime:Long = 0
    private var seconds = 0
    private var startRun = false
    private var hours = 0
    private var minutes = 0
    private var secs = 0
    var stats:MutableList<RecyclerData> = ArrayList()
    var workDay:MutableList<RecyclerData> = ArrayList()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        sp = this.activity!!.getSharedPreferences("TIMER",Context.MODE_PRIVATE)
        ed = sp.edit()

        start = view!!.findViewById(R.id.button)
        txtTime = view.findViewById(R.id.txt_clock)
        spinner = view.findViewById(R.id.project_spinner)
        testRecycler = view.findViewById(R.id.stats_recycler)
        workDesc = view!!.findViewById(R.id.txt_work)


        stats = ArrayList()

        ArrayAdapter.createFromResource(activity!!,R.array.projects_array,android.R.layout.simple_spinner_item).also {
            adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        onTimerStop()
        onTimerStart()

        seconds = sp.getInt("SECONDS",0)

//        startTime = sp.getLong("DATA(mills)",0L)
//        if(startTime!=0L){
//            val current = data.time
//            val res = ((current-startTime)/1000).toInt()
//            seconds = res
//            println("TIMER WAS STARTED")
//        }

        if (seconds!=0){
            startRun = true
            start.text = getString(R.string.stop)
        }
        start.setOnClickListener(onClick)
        return view
    }

    val onClick = View.OnClickListener { view ->
        stats = ArrayList()

        if(!startRun){
            startRun = true
            startTime = data.time
            start.text = getString(R.string.stop)
            ed.putLong("DATA(mills)",startTime).commit()
        };else{
            //start.setOnLongClickListener {
                val layoutManager = LinearLayoutManager(this.activity!!)
                testRecycler.layoutManager = layoutManager
                val adapter =  StatsAdapter(stats)
                testRecycler.adapter = adapter
                startRun = false
                ed.putString("TOTAL_TIME", txtTime.text.toString())
                workDay.add(RecyclerData(workDesc.text.toString(),spinner.selectedItem.toString(),txtTime.text.toString()))
                for (item in workDay){

                    stats.addAll(workDay)
                    adapter.updateRecycler(workDay)
                }
//                startTime = 0
//                ed.putLong("DATA(Mills)",0L)
                println(stats)
                seconds = 0
                ed.putInt("SECONDS",0).commit()
                start.text = getString(R.string.start)
              // true
            //}
        }
    }

    private fun onTimerStart(){
        sp = this.activity!!.getSharedPreferences("TIMER",Context.MODE_PRIVATE)
        ed = sp.edit()
        mRunnable = Runnable {
            hours = seconds/3600
            minutes = (seconds%3600)/60
            secs = (seconds%60)
            val time = String.format("%d:%02d:%02d", hours, minutes, secs)
            txtTime.text = time
            if (startRun){
                seconds++
                ed.putInt("SECONDS",seconds).commit()
            }
            mHandler.postDelayed(mRunnable,1000)
        }
        mHandler.postDelayed(mRunnable,1000)
    }

    private fun onTimerStop(){
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent!!.getItemAtPosition(position)
        Toast.makeText(activity!!,"Selected item is $item",Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    fun getCurrentTimeUsingDate() {
//        val date = Date()
//        val strDateFormat = "HH:mm:ss"
//        val dateFormat = SimpleDateFormat(strDateFormat)
//        val formattedDate = dateFormat.format(date)
//        println("Current time of the day using Date - 24 hour format: $formattedDate")
//            mRunnable = Runnable{
//                val current = startTime+60
//                val newFormat = dateFormat.format(Date(current))
//                mHandler.postDelayed(mRunnable,1000)
//            }
//        mHandler.postDelayed(mRunnable,1000)
//    }

}
