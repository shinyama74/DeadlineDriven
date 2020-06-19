package com.example.myoriginalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.chosen_item_task_data_cell.view.*
import kotlinx.android.synthetic.main.item_task_data_cell.view.*

class CustomChosenTaskAdapter(private val context: Context,
                              private var chosenTaskList: OrderedRealmCollection<UnSolvedTask>?,//なんだこいつは
                              private var listener: OnItemClickLisener,
                              private val autoUpdate: Boolean //trueにするとDB更新時に自動でView生成してくれる
):
    RealmRecyclerViewAdapter<UnSolvedTask,CustomChosenTaskAdapter.ViewHolder>(chosenTaskList,autoUpdate) {//やっぱりRealmRecyclerViewへ

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val TaskImage: ImageView =view.findViewById(R.id.taskImageView)
        val TaskDeadLine: TextView = view.findViewById(R.id.csdeadlineDayTextView)
        val TaskCostTime: TextView = view.findViewById(R.id.cscostTimeTextView)
        val TaskName: TextView = view.findViewById(R.id.cstaskNameTextView)

        val deleteButton: ImageView = view.csdeleteButton
//        val checkButton: ImageButton = view.checkTaskButton
        //val container : LinearLayout = view.container
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.chosen_item_task_data_cell, viewGroup, false)
        return ViewHolder(v)
    }

//    fun addAll(chosenData:MutableList<UnSolvedTask?>){
//        this.items= chosenData.toMutableList()
//    }

//      fun addAll2(items: List<UnSolvedTask>){
//          this.items.addAll(items)
//          notifyDataSetChanged()
//      }


    override fun getItemCount(): Int{
        return chosenTaskList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item :UnSolvedTask = chosenTaskList?.get(position) ?:return
        //holder.TaskImage.setImageResource(item.imageResource)
        holder.TaskName.text = item!!.taskName
        holder.TaskDeadLine.text = item.taskDeadLine
        holder.TaskCostTime.text = item.taskCostTime.toString()
        holder.deleteButton.setOnClickListener {
            listener.onItemDeleteClick(item)
        }
//        holder.checkButton.setOnClickListener {
//            listener.onItemCheckClick(item)
//        }

        //holder.container.setOnClickListener{
        //    listener.onItemClick(item)
        //}
    }

        interface OnItemClickLisener{
        fun onItemDeleteClick(item: UnSolvedTask)
        fun onItemCheckClick(item: UnSolvedTask)
    }
    //fun addAll(items: List<UnSolvedTask>?) {
    //    if (items != null) {
    //        this.items?.addAll(items)
    //    }
    //    notifyDataSetChanged()
    //}
}