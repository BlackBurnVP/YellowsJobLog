package com.example.vitalii.yellowsjoblog.WorkTime


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import java.util.*


@Suppress("DEPRECATION")
class ClockFragment : Fragment(){

    private lateinit var start:Button
    private lateinit var txtTime:TextView
    private val mHandler:Handler = Handler()
    private var mRunnable:Runnable = Runnable {  }
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor
    private val data = Date()
    private var startTime:Long = 0
    private var seconds = 0
    private var startRun = false
    private var hours = 0
    private var minutes = 0
    private var secs = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        sp = this.activity!!.getSharedPreferences("TIMER",Context.MODE_PRIVATE)
        ed = sp.edit()

        start = view!!.findViewById(R.id.button)
        txtTime = view.findViewById(R.id.txt_clock)

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
            println("Seconds != 0")
        }
        start.setOnClickListener{
            if(!startRun){
//                startTime = data.time
//                ed.putLong("DATA(mills)",startTime)
//                println("DATA(Mills) $startTime")
                start.text = getString(R.string.stop)
                startRun = true
            };else{
                seconds = 0
//                startTime = 0
//                ed.putLong("DATA(Mills)",0L)
                ed.putInt("SECONDS",0).commit()
                start.text = getString(R.string.start)
                startRun = false
            }
        }
        return view
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

    private fun onTimerStart(){
        println("OnTimerStart METHOD")
        println("StartRun = $startRun")
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
}
