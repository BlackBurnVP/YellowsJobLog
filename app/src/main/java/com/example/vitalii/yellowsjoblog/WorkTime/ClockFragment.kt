package com.example.vitalii.yellowsjoblog.WorkTime


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R

class ClockFragment : Fragment() {

    private lateinit var start:Button
    private lateinit var txtTime:TextView
    private lateinit var mHandler:Handler
    private lateinit var mRunnable:Runnable
    private var seconds = 0
    private var startRun = false
    private var hours = 0
    private var minutes = 0
    private var secs = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        start = view!!.findViewById(R.id.button)
        txtTime = view.findViewById(R.id.txt_clock)
        mHandler = Handler()

        onTimerStart()
        start.setOnClickListener{
            if(!startRun){
                start.text = getString(R.string.stop)
                startRun = true
            };else{
                start.text = getString(R.string.start)
                startRun = false
            }
        }
        return view
    }

    private fun onTimerStart(){
        mRunnable = Runnable{
            hours = seconds/3600
            minutes = (seconds%3600)/60
            secs = (seconds%60)
            txtTime.text = String.format("%d:%02d:%02d", hours, minutes, secs)
            if (startRun){
                seconds++
            }
            mHandler.postDelayed(mRunnable,1000)
        }
        mHandler.postDelayed(mRunnable,1000)
    }
}
