package com.example.vitalii.yellowsjoblog.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.Clients

class ClientsAdapter(private var clients:MutableList<Clients>? = ArrayList()) : RecyclerView.Adapter<ClientsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.clients_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tm = clients!![position]
        holder.clientName.text = tm.name
        holder.clientColor.setBackgroundColor(Color.parseColor(tm.color))
    }

    override fun getItemCount(): Int {
        return clients?.size ?:0
    }

    fun updateRecycler(newList: MutableList<Clients>){
        clients!!.clear()
        clients!!.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clientName:TextView = itemView.findViewById(R.id.txtClientsName) as TextView
        var clientColor:View = itemView.findViewById(R.id.clientColor) as View
    }
}