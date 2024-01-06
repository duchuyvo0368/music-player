package com.example.mymusicriyas

import android.app.PendingIntent
import android.app.Service
import android.app.appsearch.AppSearchSession
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.lang.Exception

class MusicService : Service() , AudioManager.OnAudioFocusChangeListener{
    private var myBinder =MyBinder()
    var mediaPlayer:MediaPlayer?=null

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    lateinit var audioManager: AudioManager

    override fun onBind(intent: Intent?): IBinder {
        mediaSession= MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }


    inner class  MyBinder: Binder(){
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn:Int){
        val intent= Intent(baseContext,MainActivity::class.java)

        val contentIntent =PendingIntent.getActivity(this,0,intent,0)

        val prevIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        val nextIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)


        val imgArt= getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)


        val image = if (imgArt !=null){
            BitmapFactory.decodeByteArray(imgArt,0,imgArt.size)
        }else{
            BitmapFactory.decodeResource(resources,R.drawable.musicshow)
        }



        val notification=NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setSmallIcon(R.drawable.music_icon)
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)

            .addAction(R.drawable.previous_icon,"previous",prevPendingIntent)

            .addAction(playPauseBtn,"play",playPendingIntent)

            .addAction(R.drawable.next_icon,"next",nextPendingIntent)
            .addAction(R.drawable.exit_icon,"exit",exitPendingIntent)
            .build()


        startForeground(13,notification)
    }
    fun createMediaPlayer(){
        try {
            if (PlayerActivity.musicService!!.mediaPlayer==null) PlayerActivity.musicService!!.mediaPlayer= MediaPlayer()
            PlayerActivity.musicService!!.mediaPlayer!!.reset()
            PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
            PlayerActivity.musicService!!.mediaPlayer!!.prepare()

            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
            PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon)


            PlayerActivity.binding.tvSeekBarStart.text= formateDuration(PlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.tvSeekBarEnd.text= formateDuration(PlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.seekBarPA.progress=0
            PlayerActivity.binding.seekBarPA.max=mediaPlayer!!.duration

            PlayerActivity.nowPlayingId = PlayerActivity.musicListPA[PlayerActivity.songPosition].id

        }catch (e: Exception){
            return
        }
    }
    fun seekBarSetup(){
        runnable= Runnable {
            PlayerActivity.binding.tvSeekBarStart.text= formateDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarPA.progress=mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }

    override fun onAudioFocusChange(focusChange : Int) {
       if (focusChange <=0){
            //pause music
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)
           NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)
            showNotification(R.drawable.play_icon)
            PlayerActivity.isPlaying =false
            mediaPlayer!!.pause()
       }else{
           //play music
           PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
           NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)
           showNotification(R.drawable.pause_icon)
           PlayerActivity.isPlaying =true
           mediaPlayer!!.start()
       }
    }
}