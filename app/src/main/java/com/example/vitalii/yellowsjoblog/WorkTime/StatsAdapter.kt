package com.example.vitalii.yellowsjoblog.WorkTime

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R

class StatsAdapter(private var stats:MutableList<RecyclerData>? = ArrayList()) : RecyclerView.Adapter<StatsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stat_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stat = stats!![position]
        holder.workDesc.text = stat.desc
        holder.project.text = stat.project
        holder.totalTime.text = stat.time
        holder.timeFlow.text = stat.timeFlow
    }

    override fun getItemCount(): Int {
        return stats?.size ?:0
    }

    fun updateRecycler(newList: MutableList<RecyclerData>){
        stats!!.clear()
        stats!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var workDesc:TextView = itemView.findViewById(R.id.txt_workDesc) as TextView
        var project:TextView = itemView.findViewById(R.id.txt_projectName) as TextView
        var timeFlow:TextView = itemView.findViewById(R.id.txt_TimeFlow) as TextView
        var totalTime:TextView = itemView.findViewById(R.id.txt_resTime) as TextView
    }
}