package com.example.myoriginalapp

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

import android.util.Log
import android.view.LayoutInflater
import android.view.View

//startActivityForResultの引数。どこで起動したアクティビティかを判別するのに用いる。
const val MY_REQUEST_CODE = 0
var chosenIdList:Array<String> = arrayOf()

class MainActivity : AppCompatActivity() {

    //val realm: Realm= Realm.getDefaultInstance()
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ダミーデータとしてコンパイル後毎回自動生成
//        create("checkedDay7プレゼン資料作り","0620",60,true)
//        create("checked月曜３限レポート","0622",45,true)

        val taskList = readAll()

        // RecycleView関連
        val adapter = CustomTaskAdapter(this, taskList,
            object : CustomTaskAdapter.OnItemClickListener{
                override fun onItemDeleteClick(item: UnSolvedTask) {
                    Toast.makeText(applicationContext, "「" + item.taskName + "」を削除しました", Toast.LENGTH_SHORT).show()
                    delete(item.id)
                }
//                override fun onItemCheckClick(item: UnSolvedTask) {
////                    update(item)
//                    Toast.makeText(applicationContext, "isChosen:"+ item.isChosen.toString(), Toast.LENGTH_SHORT).show()
//                }
                override fun onChosenItemsClick(item: UnSolvedTask, flag:Boolean) {//未選択時にクリックで「1:選択判定」、選択時にクリックで「0:未選択判定」、それ以外はエラー
                    update(item,flag)
                    Toast.makeText(applicationContext, "isChosen:"+ item.isChosen.toString(), Toast.LENGTH_SHORT).show()
                }

            }
            ,true)

        //val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)//なんだこれ
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //recyclerViewの罫線。暫定的。
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        //adapter.addAll(taskData)

        //"NEW"ボタンで新規登録画面
        val newButton = findViewById<Button>(R.id.mainToInputButton)
        newButton.setOnClickListener {
            val intentNew = Intent(this,InputTaskActivity::class.java)
            startActivityForResult(intentNew,MY_REQUEST_CODE)
        }

        //"Working"ボタンでゲーム画面へ
        val workingButton = findViewById<Button>(R.id.workingStartButton)
        workingButton.setOnClickListener {
            val intentSetting = Intent(this,SettingActivity::class.java)
            //Workingボタン押下時点でチェック済みのものを探索
//            val chosenTasksList = realm.where(UnSolvedTask::class.java).equalTo("isChosen",1.toInt()).findAll()
//            chosenTasksList.forEach{
//                chosenIdList += it.id
//            }
//            intent.putExtra("chosenIdList", chosenIdList)
            startActivityForResult(intentSetting,MY_REQUEST_CODE)
        }
    }

    //InputTaskActivity終了後はこっち
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //InputTaskActivityからの追加登録
        if(requestCode== MY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val received = data!!
            val newName = received.extras!!.get("name")
            val newDeadline = received.extras!!.get("deadline")
            val newTime = received.extras!!.get("time")
            create(newName.toString(),newDeadline.toString(),Integer.parseInt(newTime.toString()),true)
        }
    }

    //Realmの終了処理
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //Realmの呼び出し
    fun readAll() : RealmResults<UnSolvedTask> {
        return realm.where(UnSolvedTask::class.java).findAll().sort("taskRegisterDay", Sort.ASCENDING)
    }

    fun create(tskName:String, tskDeadLine:String, tskCostTime:Int, tskIsChosen:Boolean){
        realm.executeTransaction {
            val task = it.createObject(UnSolvedTask::class.java, UUID.randomUUID().toString())
                task.taskName = tskName
                task.taskDeadLine = tskDeadLine
                task.taskCostTime = tskCostTime
                task.isChosen = true
        }
    }

    fun update(item: UnSolvedTask, flag: Boolean){
        realm.executeTransaction {
            var task = realm.where(UnSolvedTask::class.java).equalTo("id",item.id).findFirst()
            if(task!!.isChosen==false) {
                task!!.isChosen=true

            }else{
                task!!.isChosen=false
            }
        }
    }

    fun delete(id: String){
        realm.executeTransaction{
            val task = realm.where(UnSolvedTask::class.java).equalTo("id" , id).findFirst()?: return@executeTransaction
            task.deleteFromRealm()
        }
    }

}
