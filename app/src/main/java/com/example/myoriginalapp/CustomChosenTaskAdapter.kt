package com.example.myoriginalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_task_data_cell.view.*
import java.nio.file.Files.find
import java.text.SimpleDateFormat
import java.util.*
import android.widget.CheckBox as CheckBox1

class CustomChosenTaskAdapter(private val context: Context,
                              //private var taskList: OrderedRealmCollection<UnSolvedTask>?//なんだこいつは
                              private var listener : CustomChosenTaskAdapter.OnItemClickLisener,
                              private val autoUpdate: Boolean //trueにするとDB更新時に自動でView生成してくれる
):
    RecyclerView.Adapter<CustomChosenTaskAdapter.ViewHolder> () {//RealmRecyclerViewからただのRecyclerViewへ

    var items: MutableList<UnSolvedTask?> =mutableListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val TaskImage: ImageView =view.findViewById(R.id.taskImageView)
        val TaskDeadLine: TextView = view.findViewById(R.id.deadlineDayTextView)
        val TaskCostTime: TextView = view.findViewById(R.id.costTimeTextView)
        val TaskName: TextView = view.findViewById(R.id.taskNameTextView)

        val deleteButton: ImageView = view.deleteButton
        val checkButton: ImageButton = view.checkTaskButton
        //val container : LinearLayout = view.container
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.chosen_item_task_data_cell, viewGroup, false)
        return ViewHolder(v)
    }

//    fun addAll(chosenData:MutableList<UnSolvedTask?>){
//        this.items= chosenData.toMutableList()
//    }

      fun addAll2(items: List<UnSolvedTask>){
          this.items.addAll(items)
          notifyDataSetChanged()
      }


    override fun getItemCount(): Int{
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : UnSolvedTask? = items[position]!!
        //holder.TaskImage.setImageResource(item.imageResource)
        holder.TaskName.text = item!!.taskName
        holder.TaskDeadLine.text = item.taskDeadLine
        holder.TaskCostTime.text = item.taskCostTime.toString()
        holder.deleteButton.setOnClickListener {
            listener.onItemDeleteClick(item)
        }
        holder.checkButton.setOnClickListener {
            listener.onItemCheckClick(item)
        }

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