package com.example.myoriginalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class SettingActivity : AppCompatActivity() {
    val chosenData: List<UnSolvedTask> = listOf(
        UnSolvedTask(UUID.randomUUID().toString(),"task1","0607",80),
        UnSolvedTask(UUID.randomUUID().toString(),"task2","0607",100),
        UnSolvedTask(UUID.randomUUID().toString(),"task3","0608",50),
        UnSolvedTask(UUID.randomUUID().toString(),"task4","0609",60)
    )
    //val taskList = readAll()
//
//    private val realm: Realm by lazy {
//        Realm.getDefaultInstance()
//    }
//
//    private val chosenRealm: Realm by lazy {
//        Realm.getDefaultInstance()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

//        val chosenIdList=intent.getStringExtra("chosenIdList")
//        chosenIdList.forEach {i-> //少なくともここでは<UnsolvedTask?>が必要
//            chosenData.add(realm.where(UnSolvedTask::class.java).equalTo("id",i.toString()).findFirst())
//        }

        val adapter = CustomChosenTaskAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.chosenRecyclerView)
        recyclerView.setHasFixedSize(true)//なんだこれ
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.addAll2(chosenData)

        //recyclerViewの罫線。暫定的。
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)


        val backButton = findViewById<Button>(R.id.settingToMainButton)
        backButton.setOnClickListener {
            finish()
        }
    }

//    //Realmの呼び出し
//    fun readAll() : RealmResults<UnSolvedTask> {
//        return realm.where(UnSolvedTask::class.java).findAll().sort("taskRegisterDay", Sort.ASCENDING)
//    }

}