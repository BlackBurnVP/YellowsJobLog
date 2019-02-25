package com.example.vitalii.yellowsjoblog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.Reports

class DashboardAdapter(private var reports:MutableList<Reports>? = ArrayList()) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stat_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stat = reports!![position]
        holder.workDesc.text = stat.name
        holder.project.text = stat.projectName
        holder.timeFlow.text = "${stat.hourstart}-${stat.hourend}"
        holder.totalTime.text = stat.datestamp
        if (stat.name!!.isEmpty()){
            holder.workDesc.text = "No description"
        }
        if (stat.projectName == null){
            holder.project.text = "No project"
        }
    }

    override fun getItemCount(): Int {
        return reports?.size ?:0
    }

    /**
     * Updating Recycler View Data with new List
     * @param newList List with new data
     */
    fun updateRecycler(newList: MutableList<Reports>){
        reports!!.clear()
        reports!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var workDesc:TextView = itemView.findViewById(R.id.txt_Name) as TextView
        var project:TextView = itemView.findViewById(R.id.txt_projectName) as TextView
        var timeFlow:TextView = itemView.findViewById(R.id.txt_TimeFlow) as TextView
        var totalTime:TextView = itemView.findViewById(R.id.txt_resTime) as TextView
    }
}