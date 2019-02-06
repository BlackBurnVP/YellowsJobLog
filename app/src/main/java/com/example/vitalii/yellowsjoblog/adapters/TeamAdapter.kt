package com.example.vitalii.yellowsjoblog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.Users

class TeamAdapter(private var team:MutableList<Users>? = ArrayList()) : RecyclerView.Adapter<TeamAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.team_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tm = team!![position]
        holder.fullName.text = tm.fullName
        holder.email.text = tm.email
        holder.role.text = tm.role
        if(tm.isActive!!){
            holder.isActive.text = "Active"
        };else{
            holder.isActive.text = "Not Active"
        }
    }

    override fun getItemCount(): Int {
        return team?.size ?:0
    }

    fun updateRecycler(newList: MutableList<Users>){
        team!!.clear()
        team!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullName:TextView = itemView.findViewById(R.id.txtName) as TextView
        var email:TextView = itemView.findViewById(R.id.txtEmail) as TextView
        var role:TextView = itemView.findViewById(R.id.txtRole) as TextView
        var isActive:TextView = itemView.findViewById(R.id.txtIsActive) as TextView
    }
}