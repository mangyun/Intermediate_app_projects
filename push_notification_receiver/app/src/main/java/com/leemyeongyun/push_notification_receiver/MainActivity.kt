package com.leemyeongyun.push_notification_receiver

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }
    private val firebaseToken: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }

    // 순서상 바로 이전에 했던 알림을 다시 호출한, single타입일 경우
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)//새로운 정보인 새 intent로 교체
        updateResult(true)
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

    @SuppressLint("SetTextI18n")//다국어 처리
    private fun updateResult(isNewIntent: Boolean = false) {
        resultTextView.text =
            (intent.getStringExtra("notificationType") ?: "앱 런처") +//타입이 있다면 쓰고, null이면 앱 런처를 누른것임
                    if (isNewIntent) {//앱이 켜져있다면,
                        "(으)로 갱신했습니다."
                    } else {//앱이 새로 실행되었다면
                        "(으)로 실행했습니다."
                    }
    }
}