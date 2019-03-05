package com.example.vitalii.yellowsjoblog.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.example.vitalii.yellowsjoblog.R
import com.example.vitalii.yellowsjoblog.api.Reports

class DashboardAdapter(private var reports:MutableList<Reports>? = ArrayList()) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.stat_view, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stat = reports!![position]
        holder.workDesc.text = stat.name
        holder.project.text = stat.projectName
        holder.timeFlow.text = "${stat.hourStart}-${stat.hourEnd}"
        holder.totalTime.text = stat.datestamp
        if (stat.name == null || stat.name!!.isEmpty()){
            holder.workDesc.text = "No description"
        }
        if (stat.projectName == null){
            holder.project.text = "No project"
        }
        if (stat.hourEnd == null){
            holder.timeFlow.text ="${stat.hourStart}- "
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

/**
 * Click listener class
 */
class ClickListener(context: Context, recyclerView: RecyclerView, private val mListener: OnItemClickListener?) :
    RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetector

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)

        fun onLongItemClick(view: View?, position: Int)
    }

    init {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}