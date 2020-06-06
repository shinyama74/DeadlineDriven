package com.example.myoriginalapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.read
import android.text.Editable
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_input_task.*

class InputTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_task)

        val registerButton = findViewById<Button>(R.id.InputCompleteButton)
        registerButton.setOnClickListener {

            //入力内容を変数に格納
            val tskName:String? = InputTaskNameTextView.text.toString()
            val tskDeadLine:String? = InputTaskDeadlineTextView.text.toString()
            val tskCostTime: String? = InputTaskCostTimeTextView.text.toString()

            if(tskName == null || tskDeadLine == null || tskCostTime == null){
                Toast.makeText(applicationContext, "入力が正しくありません", Toast.LENGTH_LONG).show()
            }else{
                //MainActivityへもっていく
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("name",tskName)
                intent.putExtra("deadline",tskDeadLine)
                intent.putExtra("time",tskCostTime)

                Toast.makeText(applicationContext, "タスクを登録しました", Toast.LENGTH_LONG).show()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        val backButton = findViewById<Button>(R.id.inputToMainButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
