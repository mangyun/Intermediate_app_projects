package com.leemyeongyun.push_notification_receiver

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /*토큰을 갱신 처리를 해줘야함(이 프로젝트에는 할필요가 없어서 안했음)
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }*/

    //firebase에서 메세지를 수신할때마다 호출됨
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }


}