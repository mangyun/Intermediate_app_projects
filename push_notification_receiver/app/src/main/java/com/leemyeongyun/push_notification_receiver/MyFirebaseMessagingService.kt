package com.leemyeongyun.push_notification_receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    //이 프로젝트에는 할필요가 없어서 안했음
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    //firebase에서 메세지를 수신할때마다 호출됨
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()//일단 먼저 채널 생성

        //api의 title과 message, type을 가져옴
        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return //타입이 없을 경우, 데이터가 유효하지 않아 반환

        //메세지를 보냈을 때, 제목과 내용에 맞춰서 알림을 보냄
        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    //채널 생성
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// 오레오 버전 이상일 경우
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.description = CHANNEL_DESCRIPTION // 채널설명

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) //notification 매니저에 추가
                .createNotificationChannel(channel) //그 매니저에 생성한 채널을 넣어줌
        }
    }

    //알림 만들기
    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?,
    ): Notification {
        //탭 반응을 위한 인텐트 생성
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)// 순서상 바로 이전에 했던 알림을 다시 호출하면, 그냥 갱신함
        }

        //인텐트를 사용할 수 있는 권한을 준다고 생각하면 됨. type.id와 flag_update_current로 각 id에 맞는 메세지 형태를 보냄
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications) //알림 아이콘
            .setContentTitle(title) //제목
            .setContentText(message) //내용 전달
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //중요도를 7.1이하 버전에서는 별도로 priority를 지정해야함
            .setContentIntent(pendingIntent)//type별로 pending인텐트를 만들어서 전달
            .setAutoCancel(true)//클릭했을 때 알림 지우기

        when (type) {
            NotificationType.NORMAL -> Unit // 아무것도 안함

            NotificationType.EXPANDABLE -> {//확장형으로 알림 보내기
                notificationBuilder.setStyle(//메시지 종류 설정
                    NotificationCompat.BigTextStyle()
                        .bigText("\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83D\uDE02 \uD83E\uDD23 \uD83E\uDD72 ☺️ \uD83D\uDE0A \uD83D\uDE07 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0C \uD83D\uDE0D \uD83E\uDD70 \uD83D\uDE18 \uD83D\uDE17 \uD83D\uDE19 \uD83D\uDE1A \uD83D\uDE0B \uD83D\uDE1B " +
                                "\uD83D\uDE1D \uD83D\uDE1C \uD83E\uDD2A \uD83E\uDD28 \uD83E\uDDD0 \uD83E\uDD13 \uD83D\uDE0E \uD83E\uDD78 \uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12 \uD83D\uDE1E \uD83D\uDE14 \uD83D\uDE1F \uD83D\uDE15 \uD83D\uDE41 ☹️" +
                                " \uD83D\uDE23 \uD83D\uDE16 \uD83D\uDE2B \uD83D\uDE29 \uD83E\uDD7A \uD83D\uDE22 \uD83D\uDE2D \uD83D\uDE24 \uD83D\uDE20 \uD83D\uDE21 \uD83E\uDD2C \uD83E\uDD2F \uD83D\uDE33 \uD83E\uDD75 \uD83E\uDD76 \uD83D\uDE31 \uD83D\uDE28 \uD83D\uDE30 \uD83D\uDE25 \uD83D\uDE13 \uD83E\uDD17 \uD83E\uDD14 \uD83E\uDD2D \uD83E\uDD2B" +
                                "\uD83E\uDD25 \uD83D\uDE36 \uD83D\uDE10 \uD83D\uDE11 \uD83D\uDE2C \uD83D\uDE44 \uD83D\uDE2F \uD83D\uDE26 \uD83D\uDE27 \uD83D\uDE2E \uD83D\uDE32 \uD83E\uDD71 \uD83D\uDE34 \uD83E\uDD24 \uD83D\uDE2A \uD83D\uDE35 \uD83E\uDD10 \uD83E\uDD74")
                )

            }

            NotificationType.CUSTOM -> {//CUSTOM형식으로 알림 보내기
                notificationBuilder.setStyle(
                    NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName, R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title) //이렇게 해야 xml title 값에 해당 title이 들어감
                            setTextViewText(R.id.message, message) //위와 마찬가지
                        }
                    )
            }
        }
        return notificationBuilder.build() //반환
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }

}


