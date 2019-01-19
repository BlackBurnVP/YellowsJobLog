package com.example.vitalii.yellowsjoblog.WorkTime


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleExpandableListAdapter

import com.example.vitalii.yellowsjoblog.R
import android.widget.ExpandableListView



class StatsFragment : Fragment() {

    private val mGroupsArray = arrayOf("Зима", "Весна", "Лето", "Осень")

    private val mWinterMonthsArray = arrayOf("Декабрь", "Январь", "Февраль")
    private val mSpringMonthsArray = arrayOf("Март", "Апрель", "Май")
    private val mSummerMonthsArray = arrayOf("Июнь", "Июль", "Август")
    private val mAutumnMonthsArray = arrayOf("Сентябрь", "Октябрь", "Ноябрь")

    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mRefresh:Button

    var stats:ArrayList<RecyclerData> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        stats = ArrayList()

        mRefresh = view!!.findViewById(R.id.btn_refresh)
        mRecyclerView = view!!.findViewById(R.id.statistic)
        mRefresh.setOnClickListener (onClick)

//        var map: MutableMap<String, String>
//        // коллекция для групп
//        val groupDataList = ArrayList<MutableMap<String,String>>()
//        // заполняем коллекцию групп из массива с названиями групп
//
//        for (group in mGroupsArray) {
//            // заполняем список атрибутов для каждой группы
//            map = HashMap()
//            map["groupName"] = group // время года
//            groupDataList.add(map)
//        }
//
//        // список атрибутов групп для чтения
//        val groupFrom = arrayOf("groupName")
//        // список ID view-элементов, в которые будет помещены атрибуты групп
//        val groupTo = intArrayOf(android.R.id.text1)
//
//        // создаем общую коллекцию для коллекций элементов
//        val сhildDataList = ArrayList<ArrayList<Map<String,String>>>()
//
//        // в итоге получится сhildDataList = ArrayList<сhildDataItemList>
//
//        // создаем коллекцию элементов для первой группы
//        var сhildDataItemList: ArrayList<Map<String, String>> = ArrayList()
//        // заполняем список атрибутов для каждого элемента
//        for (month in mWinterMonthsArray) {
//            map = HashMap()
//            map["monthName"] = month // название месяца
//            сhildDataItemList.add(map)
//        }
//        // добавляем в коллекцию коллекций
//        сhildDataList.add(сhildDataItemList)
//
//        // создаем коллекцию элементов для второй группы
//        сhildDataItemList = ArrayList()
//        for (month in mSpringMonthsArray) {
//            map = HashMap()
//            map["monthName"] = month
//            сhildDataItemList.add(map)
//        }
//        сhildDataList.add(сhildDataItemList)
//
//        // создаем коллекцию элементов для третьей группы
//        сhildDataItemList = ArrayList()
//        for (month in mSummerMonthsArray) {
//            map = HashMap()
//            map["monthName"] = month
//            сhildDataItemList.add(map)
//        }
//        сhildDataList.add(сhildDataItemList)
//
//        // создаем коллекцию элементов для четвертой группы
//        сhildDataItemList = ArrayList()
//        for (month in mAutumnMonthsArray) {
//            map = HashMap()
//            map["monthName"] = month
//            сhildDataItemList.add(map)
//        }
//        сhildDataList.add(сhildDataItemList)
//
//        // список атрибутов элементов для чтения
//        val childFrom = arrayOf("monthName")
//        // список ID view-элементов, в которые будет помещены атрибуты
//        // элементов
//        val childTo = intArrayOf(android.R.id.text1)
//
//        val adapter = SimpleExpandableListAdapter(
//            activity, groupDataList,
//            android.R.layout.simple_expandable_list_item_1, groupFrom,
//            groupTo, сhildDataList, android.R.layout.simple_list_item_1,
//            childFrom, childTo
//        )
//
//        val expandableListView = view.findViewById(R.id.statList) as ExpandableListView
//        expandableListView.setAdapter(adapter)
        return view
    }

    val onClick = View.OnClickListener { view ->
        val layoutManager = LinearLayoutManager(this.activity!!)
        mRecyclerView.layoutManager = layoutManager
        val adapter = StatsAdapter(stats)
        mRecyclerView.adapter = adapter
        println(ClockFragment().stats)


        for (item in ClockFragment().workDay){
            stats.addAll(ClockFragment().workDay)
            adapter.updateRecycler(ClockFragment().workDay)
        }
    }
}
