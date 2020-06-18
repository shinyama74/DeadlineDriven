package com.example.myoriginalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_task_data_cell.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomTaskAdapter(private val context: Context,
                        private var taskList: OrderedRealmCollection<UnSolvedTask>?,//なんだこいつは.MainActivityでrealmResultをfindAllして得られるやつ
                        private var listener : OnItemClickListener,
                        private val autoUpdate: Boolean //trueにするとDB更新時に自動でView生成してくれる
                        ):
    RealmRecyclerViewAdapter<UnSolvedTask,CustomTaskAdapter.ViewHolder> (taskList,autoUpdate) {

    //val items: MutableList<UnSolvedTask>? = mutableListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val TaskImage: ImageView =view.findViewById(R.id.taskImageView)
        val TaskDeadLine: TextView = view.findViewById(R.id.deadlineDayTextView)
        val TaskCostTime: TextView = view.findViewById(R.id.costTimeTextView)
        val TaskName: TextView = view.findViewById(R.id.taskNameTextView)

        val deleteButton: ImageView = view.deleteButton
        val checkButton: ImageButton = view.checkTaskButton

        val checkTaskBox:CheckBox = view.findViewById(R.id.taskCheckBox)
        //val container : LinearLayout = view.container
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_task_data_cell, viewGroup, false)
        return ViewHolder(v)
    }

    interface OnItemClickListener{
        fun onItemDeleteClick(item: UnSolvedTask)
        fun onItemCheckClick(item: UnSolvedTask, flag:Boolean)
        fun onChosenItemsClick(item: UnSolvedTask, flag:Boolean)
    }

    override fun getItemCount(): Int = taskList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item :UnSolvedTask = taskList?.get(position) ?:return
        //holder.TaskImage.setImageResource(item.imageResource)
        holder.TaskName.text = item.taskName
        holder.TaskDeadLine.text = item.taskDeadLine
        holder.TaskCostTime.text = item.taskCostTime.toString()
        var flag = holder.checkTaskBox.isChecked

        holder.deleteButton.setOnClickListener {
            listener.onItemDeleteClick(item)
        }
        holder.checkButton.setOnClickListener {
            listener.onItemCheckClick(item,flag)
        }
        holder.checkTaskBox.setOnClickListener{
            listener.onChosenItemsClick(item,flag)
        }
//        //holder.container.setOnClickListener{
        //    listener.onItemClick(item)
        //}

    }

    //fun addAll(items: List<UnSolvedTask>?) {
    //    if (items != null) {
    //        this.items?.addAll(items)
    //    }
    //    notifyDataSetChanged()
    //}
}