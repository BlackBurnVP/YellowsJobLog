package com.example.vitalii.yellowsjoblog.WorkTime

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ViewPagerAdapter (mFragmentManger:FragmentManager?):FragmentStatePagerAdapter(mFragmentManger) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment? {
        when(position) {
            0 -> return ClockFragment()
            1 -> return StatsFragment()
        }
        return null
    }
}