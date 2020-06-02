package com.example.myoriginalapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.file.Files.delete
import java.util.*

//startActivityForResultの引数。どこで起動したアクティビティかを判別するのに用いる。
const val MY_REQUEST_CODE = 0

class MainActivity : AppCompatActivity() {

    //val realm: Realm= Realm.getDefaultInstance()
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        create("Day8プレゼン資料作り","0620",60)
        //create("月曜３限レポート","0622",45)

        //空の場合のダミーデータ
        //if(taskList.isEmpty()){
        //}

        val taskList = readAll()

        // RecycleView関連
        val adapter = CustomTaskAdapter(this, taskList,
            object : CustomTaskAdapter.OnItemClickLisener{
                override fun onItemClick(item: UnSolvedTask, clickedText: String) {
                    TODO("Not yet implemented")
                }

                override fun onItemClick(item: UnSolvedTask) {
                    Toast.makeText(applicationContext, "「" + item.taskName + "」を削除しました", Toast.LENGTH_SHORT).show()
                    delete(item.id)
                }
            }
            ,true)
        //val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)//なんだこれ
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        //adapter.addAll(taskData)

        //"NEW"ボタンで新規登録画面
        val newButton = findViewById<Button>(R.id.mainToInputButton)
        newButton.setOnClickListener {
            val intent = Intent(this,InputTaskActivity::class.java)
            startActivityForResult(intent,MY_REQUEST_CODE)
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
            create(newName.toString(),newDeadline.toString(),Integer.parseInt(newTime.toString()))
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

    fun create(tskName:String, tskDeadLine:String, tskCostTime:Int){
        realm.executeTransaction {
            val task = it.createObject(UnSolvedTask::class.java, UUID.randomUUID().toString())
                task.taskName = tskName
                task.taskDeadLine = tskDeadLine
                task.taskCostTime = tskCostTime
        }
    }

    fun delete(id: String){
        realm.executeTransaction{
            val task = realm.where(UnSolvedTask::class.java).equalTo("id" , id).findFirst()?: return@executeTransaction
            task.deleteFromRealm()
        }
    }


}
