package com.example.vitalii.yellowsjoblog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.Reports


class ReportsAdapter(private var reports:MutableList<Reports>? = ArrayList()) : RecyclerView.Adapter<ReportsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reports_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports!![position]
        val data:String? = null
        val parentID = report.id
        holder.txtName.text = report.user
        holder.txtDesc.text = report.name
//            if(data == report.datestampstart || data == report.datestampend || data == report.projectName){
//                holder.txtProject.setPadding(80,0,0,0)
//                holder.txtDate.setPadding(80,0,0,0)
//            }

        holder.txtProject.text = report.projectName
        holder.txtDate.text = report.date
        holder.txtStart.text = report.hourstart
        holder.txtEnd.text = report.hourend
        holder.txtTotal.text = report.datestamp
    }

    override fun getItemCount(): Int {
        return reports?.size ?:0
    }

    fun updateRecycler(newList: MutableList<Reports>){
        reports!!.clear()
        reports!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName:TextView = itemView.findViewById(R.id.txtName) as TextView
        var txtDesc:TextView = itemView.findViewById(R.id.txtWorkDesc) as TextView
        var layout:LinearLayout = itemView.findViewById(R.id.layout) as LinearLayout
        var txtProject:TextView = itemView.findViewById(R.id.txtProject) as TextView
        var txtDate:TextView = itemView.findViewById(R.id.txtDate) as TextView
        var txtStart:TextView = itemView.findViewById(R.id.txtStart) as TextView
        var txtEnd:TextView = itemView.findViewById(R.id.txtEnd) as TextView
        var txtTotal:TextView = itemView.findViewById(R.id.txtTotal) as TextView
//        var totalTime:TextView = itemView.findViewById(R.id.txtTime) as TextView
    }
}