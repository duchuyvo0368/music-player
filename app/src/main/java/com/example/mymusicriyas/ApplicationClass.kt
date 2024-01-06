package com.example.mymusicriyas

import android.R
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat


class ApplicationClass : Application(){

    companion object{
        const val CHANNEL_ID="channel1"
        const val PLAY="play"
        const val NEXT="next"
        const val PREVIOUS="previous"
        const val EXIT="exit"


    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val notificationChannel= NotificationChannel(CHANNEL_ID,"Now Playing Song",
                                            NotificationManager.IMPORTANCE_NONE)


//            notificationChannel.lightColor = Color.BLUE //optional
//                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE //optional


            notificationChannel.description="This is a Important channel for showing song!!"


//            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.getNotificationChannel(notificationChannel.toString())

            val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)




        }
    }

}