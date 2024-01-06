package com.example.mymusicriyas

import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mymusicriyas.databinding.ActivityPlaylistDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder

class PlaylistDetails : AppCompatActivity() {
    lateinit var binding: ActivityPlaylistDetailsBinding
    lateinit var adapter:MusicAdapter
    companion object{
        var currentPlaylistPos:Int =-1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



            setTheme(R.style.coolPink)
            binding= ActivityPlaylistDetailsBinding.inflate(layoutInflater)
            setContentView(binding.root)
            currentPlaylistPos=intent.extras?.get("index") as Int
            PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist =
                checkPlaylist(playlist = PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist)
            binding.playlistDetailsRV.setItemViewCacheSize(10)
            binding.playlistDetailsRV.setHasFixedSize(true)
            binding.playlistDetailsRV.layoutManager = LinearLayoutManager(this)

            adapter = MusicAdapter(this,PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist, playlistDetails = true)
            binding.playlistDetailsRV.adapter =adapter

            binding.backBtnPD.setOnClickListener {
                finish()
            }
        binding.shuffleBtnPD.setOnClickListener {

            val pl= Intent(this,PlayerActivity::class.java)

            intent.putExtra("index",0)
            intent.putExtra("class","PlaylistDetailsShuffle")

            startActivity(pl)
        }
        binding.addBtnPD.setOnClickListener {
            startActivity(Intent(this,SelectionActivity::class.java))
        }
        binding.removeBtnPD.setOnClickListener {
            val builder= MaterialAlertDialogBuilder(this)
            builder.setTitle("Remove...!!")
                .setMessage("Do you want Remove All songs from Playlist")
                .setPositiveButton("Yes"){ dialog,_->
                    PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist.clear()
                    adapter.refreshPlaylist()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog,_->
                    dialog.dismiss()
                }
            val customDialog=builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        }
        }

        override fun onResume() {
            super.onResume()
            binding.playlistNamePD.text =
                PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].name
            binding.moreInfoPD.text = "Total ${adapter.itemCount} Songs. \n\n " +
                    "Created on  :  \n ${PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].createdOn}" +
                    " ,  \n ${PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].createdBy}"
            if (adapter.itemCount > 0) {
                Glide.with(this)
                    .load(PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist[0].artUri)
                    .apply(RequestOptions().placeholder(R.drawable.musicshow).centerCrop())
                    .into(binding.playlistImgPD)
                binding.shuffleBtnPD.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()

            //for storing favourites using shared preferences
            val editor= getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit()

            val jsonStringPlaylist = GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
            editor.putString("MusicPlaylist",jsonStringPlaylist)
            editor.apply()
        }
}