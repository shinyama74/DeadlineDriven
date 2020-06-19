package com.example.myoriginalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FailureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_failure)

        val backButton = findViewById<Button>(R.id.faikureToMainButton)
        backButton.setOnClickListener {
            val intentToMain = Intent(this,MainActivity::class.java)
            startActivityForResult(intentToMain,MY_REQUEST_CODE)
        }
    }
}
