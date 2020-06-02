package com.example.myoriginalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_task_data_cell.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomTaskAdapter(private val context: Context,
                        private var taskList: OrderedRealmCollection<UnSolvedTask>?,//なんだこいつは
                        private var listener : OnItemClickLisener,
                        private val autoUpdate: Boolean //trueにするとDB更新時に自動でView生成してくれる
                        ):
    RealmRecyclerViewAdapter<UnSolvedTask,CustomTaskAdapter.ViewHolder> (taskList,autoUpdate) {

    //val items: MutableList<UnSolvedTask>? = mutableListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val TaskImage: ImageView =view.findViewById(R.id.taskImageView)
        val TaskDeadLine: TextView = view.findViewById(R.id.deadlineDayTextView)
        val TaskCostTime: TextView = view.findViewById(R.id.costTimeTextView)
        val TaskName: TextView = view.findViewById(R.id.taskNameTextView)

        val container : LinearLayout = view.container
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_task_data_cell, viewGroup, false)
        return ViewHolder(v)
    }

    interface OnItemClickLisener{
        fun onItemClick(item: UnSolvedTask, clickedText: String)
        abstract fun onItemClick(item: UnSolvedTask)
    }

    override fun getItemCount(): Int = taskList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item :UnSolvedTask = taskList?.get(position) ?:return
        //holder.TaskImage.setImageResource(item.imageResource)
        holder.TaskName.text = item.taskName
        holder.TaskDeadLine.text = item.taskDeadLine
        holder.TaskCostTime.text = item.taskCostTime.toString()
        holder.container.setOnClickListener{
            listener.onItemClick(item)
        }

    }

    //fun addAll(items: List<UnSolvedTask>?) {
    //    if (items != null) {
    //        this.items?.addAll(items)
    //    }
    //    notifyDataSetChanged()
    //}
}