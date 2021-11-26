package com.leemyeongyun.push_notification_receiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val resultText: TextView by lazy {
        findViewById(R.id.resultTextView)
    }
    private val firebaseToken: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
    }

    //firebase 토큰 가져오기
    private fun initFirebase() {
        //firebase의 토큰을 가져오고, 작업이다보니 리스너를 통해야함
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->

            if (task.isSuccessful) { //작업이 성공하면
                firebaseToken.text = task.result // 토큰을 가져옴
            }
        }
    }
}