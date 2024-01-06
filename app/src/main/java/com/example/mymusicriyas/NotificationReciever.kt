package com.example.mymusicriyas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReciever :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS-> {
                prevNextSong(increment = false,context=context!!)
                //Toast.makeText(context, "previous Clicked", Toast.LENGTH_SHORT).show()
            }
            ApplicationClass.PLAY->if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT-> {
                prevNextSong(increment = true,context=context!!)
//                Toast.makeText(context, "next Clicked", Toast.LENGTH_SHORT).show()
            }
            ApplicationClass.EXIT-> {
                exitApplication()

            }

        }
    }
    private fun playMusic()
    {
        PlayerActivity.isPlaying=true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)

        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)

    }
    private fun pauseMusic()
    {
        PlayerActivity.isPlaying=false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)

        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon)

    }
    private fun prevNextSong(increment:Boolean ,context: Context){
        setSongPosition(increment=increment)

        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.musicshow).centerCrop())
            .into(PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text= PlayerActivity.musicListPA[PlayerActivity.songPosition].title


        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.musicshow).centerCrop())
            .into(NowPlaying.binding.songImgNP)

        NowPlaying.binding.songNameNP.text =PlayerActivity.musicListPA[PlayerActivity.songPosition].title

        playMusic()

        PlayerActivity.fIndex = favouriteChecker(PlayerActivity.musicListPA[PlayerActivity.songPosition].id)
        if (PlayerActivity.isFavourite) PlayerActivity.binding.favoritesBtnPA.setImageResource(R.drawable.favorite_icon)
        else PlayerActivity.binding.favoritesBtnPA.setImageResource(R.drawable.favourite_empty_icon)


    }
}