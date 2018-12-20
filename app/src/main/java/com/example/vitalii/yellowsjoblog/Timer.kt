package com.example.vitalii.yellowsjoblog

import android.os.Handler

class Timer {

    private var mHandler = Handler()
    private lateinit var mRunnable: Runnable
    var seconds = 0
    var startRun = false
    var hours = 0
    var minutes = 0
    var secs = 0
    var time = ""

    fun Timer(){
        mRunnable = Runnable{
            hours = seconds/3600
            minutes = (seconds%3600)/60
            secs = (seconds%60)
            time = String.format("%d:%02d:%02d", hours, minutes, secs)
            if (startRun){
                seconds++
            }
            mHandler.postDelayed(mRunnable,1000)
        }
        mHandler.postDelayed(mRunnable,1000)
    }
}