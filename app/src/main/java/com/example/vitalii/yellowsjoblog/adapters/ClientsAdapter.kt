package com.example.vitalii.yellowsjoblog.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.ClientsPOKO

class ClientsAdapter(private var projects:MutableList<ClientsPOKO>? = ArrayList()) : RecyclerView.Adapter<ClientsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.clients_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tm = projects!![position]
        holder.clientName.text = tm.name
        holder.clientColor.setBackgroundColor(Color.parseColor(tm.color))
//        holder.role.text = tm.role
//        if(tm.isActive!!){
//            holder.isActive.text = "Active"
//        };else{
//            holder.isActive.text = "Not Active"
//        }
    }

    override fun getItemCount(): Int {
        return projects?.size ?:0
    }

    fun updateRecycler(newList: MutableList<ClientsPOKO>){
        projects!!.clear()
        projects!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clientName:TextView = itemView.findViewById(R.id.txtClientsName) as TextView
        var clientColor:View = itemView.findViewById(R.id.clientColor) as View
        //var role:TextView = itemView.findViewById(R.id.txtRole) as TextView
        //var isActive:TextView = itemView.findViewById(R.id.txtIsActive) as TextView
    }
}