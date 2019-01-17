package com.example.vitalii.yellowsjoblog.WorkTime


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.LoginActivity

import com.example.vitalii.yellowsjoblog.R

class WorkTimeFragment : Fragment() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout:TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_work_time, container, false)
        mViewPager = view!!.findViewById(R.id.view_pager)
        mViewPager.adapter = ViewPagerAdapter(childFragmentManager)
        mTabLayout = view.findViewById(R.id.tabLayout)
        mTabLayout.setupWithViewPager(mViewPager,true)
//        mName = view!!.findViewById(R.id.view_name)
//        mName.text = LoginActivity().email
        return view
    }

    
}
