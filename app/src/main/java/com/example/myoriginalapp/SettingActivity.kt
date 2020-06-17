package com.example.myoriginalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class SettingActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

//        val chosenIdList=intent.getStringExtra("chosenIdList")
//        chosenIdList.forEach {i-> //少なくともここでは<UnsolvedTask?>が必要
//            chosenData.add(realm.where(UnSolvedTask::class.java).equalTo("id",i.toString()).findFirst())
//        }
        val chosenTaskList = readAll()

        val adapter = CustomChosenTaskAdapter(this, chosenTaskList,
            object : CustomChosenTaskAdapter.OnItemClickLisener{
                override fun onItemDeleteClick(item: UnSolvedTask) {
                    Toast.makeText(applicationContext, "「" + item.taskName + "」を削除しました", Toast.LENGTH_SHORT).show()
                    delete(item.id)
                }
                override fun onItemCheckClick(item: UnSolvedTask) {
                    Toast.makeText(applicationContext, "チェックしました", Toast.LENGTH_SHORT).show()
                }
            }
            ,true)
        val recyclerView = findViewById<RecyclerView>(R.id.chosenRecyclerView)
        recyclerView.setHasFixedSize(true)//なんだこれ
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
//        adapter.addAll2(chosenData)

        //recyclerViewの罫線。暫定的。
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)


        val backButton = findViewById<Button>(R.id.settingToMainButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    //Realmの呼び出し.
    fun readAll() : RealmResults<UnSolvedTask> {
        return realm.where(UnSolvedTask::class.java).equalTo("isChosen" ,true).findAll().sort("taskCostTime", Sort.ASCENDING)
    }

    fun delete(id: String){
    }
}
