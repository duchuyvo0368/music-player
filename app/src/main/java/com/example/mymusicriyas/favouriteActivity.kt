package com.example.mymusicriyas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicriyas.databinding.ActivityFavouriteBinding

class favouriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavouriteAdapter
    private lateinit var binding:ActivityFavouriteBinding

    companion object{
        var favouriteSongs: ArrayList<Music> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_MyMusicRiyas)
        binding= ActivityFavouriteBinding.inflate(layoutInflater)

//        setContentView(R.layout.activity_favourite)
        setContentView(binding.root)
        favouriteSongs = checkPlaylist(favouriteSongs)
        binding.backBtnFA.setOnClickListener{
            finish()
        }
        binding.favouriteRV.setHasFixedSize(true)
        binding.favouriteRV.setItemViewCacheSize(13)
        binding.favouriteRV.layoutManager= GridLayoutManager(this,3)
        adapter= FavouriteAdapter(this, favouriteSongs )
        binding.favouriteRV.adapter=adapter

        if(favouriteSongs.size <1) binding.shuffleBtnFA.visibility = View.INVISIBLE
        binding.shuffleBtnFA.setOnClickListener {

            val pl= Intent(this,PlayerActivity::class.java)

            intent.putExtra("index",0)
            intent.putExtra("class","FavouriteShuffle")

            startActivity(pl)
        }


    }
}