package com.example.vitalii.yellowsjoblog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.Projects

class ProjectsAdapter(private var projects:MutableList<Projects>? = ArrayList()) : RecyclerView.Adapter<ProjectsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.projects_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tm = projects!![position]
        holder.projectName.text = tm.name
        holder.clientName.text = tm.client!!.get(position).name
    }

    override fun getItemCount(): Int {
        return projects?.size ?:0
    }

    fun updateRecycler(newList: MutableList<Projects>){
        projects!!.clear()
        projects!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var projectName:TextView = itemView.findViewById(R.id.txtProjectName) as TextView
        var clientName:TextView = itemView.findViewById(R.id.txtClientName) as TextView
    }
}