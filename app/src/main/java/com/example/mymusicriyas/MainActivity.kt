package com.example.mymusicriyas

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView

import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicriyas.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var  toggle:ActionBarDrawerToggle

    private lateinit var musicAdapter: MusicAdapter

    companion object{
      lateinit  var MusicListMA:ArrayList<Music>
      lateinit var musicListSearch:ArrayList<Music>
      var search:Boolean=false
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_MyMusicRiyas)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //for nav drawer
        toggle= ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(requestRuntimePermission()){
            initializeLayout()
            initialAfter()

        }





        binding.shuffleBtn.setOnClickListener {
            val pl=Intent(this@MainActivity,PlayerActivity::class.java)

            intent.putExtra("index",0)
            intent.putExtra("class","MainActivity")

            startActivity(pl)
//            Toast.makeText(this@MainActivity,"shuffle Button clicked",Toast.LENGTH_SHORT).show()

        }

        binding.playlistsBtn.setOnClickListener {
            val ii=Intent(this@MainActivity,PlaylistActivity::class.java)
            startActivity(ii)
            //            Toast.makeText(this@MainActivity,"shuffle Button clicked",Toast.LENGTH_SHORT).show()
        }

        binding.favoritesBtn.setOnClickListener {

            startActivity(Intent(this@MainActivity,favouriteActivity::class.java))
            //            Toast.makeText(this@MainActivity,"shuffle Button clicked",Toast.LENGTH_SHORT).show()
        }


        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.navFeedback->{
                    startActivity(Intent(this@MainActivity,FeedbackActivity::class.java))
                }
//                    Toast.makeText(baseContext,"Feed back",Toast.LENGTH_SHORT).show()

                R.id.navSettings->{
//                    startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
                }
//                    Toast.makeText(baseContext,"Setting",Toast.LENGTH_SHORT).show()

                R.id.navAbout->{
                    startActivity(Intent(this@MainActivity,AboutActivity::class.java))
                }

//                    Toast.makeText(baseContext,"About",Toast.LENGTH_SHORT).show()

                R.id.navExit->{
                     val builder=MaterialAlertDialogBuilder(this)
                    builder.setTitle("Exit")
                        .setMessage("Do you want Close")
                        .setPositiveButton("Yes"){_,_->
                          exitApplication()
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
            true
        }


    }

//For requesting Permission
    private fun requestRuntimePermission() : Boolean{
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
             !=PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)

            initializeLayout()
            initialAfter()
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==13){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
                MusicListMA =getAllAudio()
            }

            else
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun initializeLayout(){

        search=false
        MusicListMA=getAllAudio()

        val musicList=ArrayList<String>()

        /*
        musicList.add(" Song 1")
        musicList.add(" Song 2")
        musicList.add(" Song 3")
        musicList.add(" Song 4")
        musicList.add(" Song 5")
        musicList.add(" Song 6")
        musicList.add(" Song 7")
        musicList.add(" Song 8")
        musicList.add(" Song 9")
        musicList.add(" Song 10")
        musicList.add(" Song 11")
        musicList.add(" Song 12")
        musicList.add(" Song 13")
        musicList.add(" Song 14")
        musicList.add(" Song 15")*/

        MusicListMA=getAllAudio()





        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.setItemViewCacheSize(13)
        binding.musicRV.layoutManager=LinearLayoutManager(this@MainActivity)
        musicAdapter= MusicAdapter(this@MainActivity, MusicListMA)
        binding.musicRV.adapter=musicAdapter

        binding.totalSongs.text="Total songs : "+musicAdapter.itemCount



    }
//    @requiresApi(Build.VERSION_CODES.R)
    @SuppressLint("Range")
    private fun getAllAudio():ArrayList<Music>{
        val tempList=ArrayList<Music>()
        val selection=MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val  projection= arrayOf(MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.ALBUM,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.DURATION,
                                MediaStore.Audio.Media.DATE_ADDED,
                                MediaStore.Audio.Media.DATA,
                                MediaStore.Audio.Media.ALBUM_ID)
        val cursor=this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            projection,selection,null,MediaStore.Audio.Media.DATE_ADDED + " DESC",
                                            null)

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val titleC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val pathC=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    val albumIdC=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri=Uri.parse("content://media/external/audio/albumart")
                    val artUriC=Uri.withAppendedPath(uri,albumIdC).toString()


                    val music=Music(id=idC,title=titleC, album = albumC, artist = artistC,path=pathC,
                                    duration = durationC, artUri = artUriC)

                    val file=File(music.path)

                    if (file.exists()){
                        tempList.add(music)
                    }


                }while (cursor.moveToNext())
                cursor.close()
            }

        }







        return tempList
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!PlayerActivity.isPlaying && PlayerActivity.musicService != null){
            exitApplication()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu,menu)
        val searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String?): Boolean =true

            override fun onQueryTextChange(newText : String?): Boolean {
                musicListSearch=ArrayList()

                if (newText!=null){
                    var userInput=newText.lowercase()
                    for (song in MusicListMA)
                        if (song.title.lowercase().contains(userInput))
                            musicListSearch.add(song)

                    search=true
                    musicAdapter.updateMusicList(searchList = musicListSearch)
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()

        val editor= getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit()
        val jsonString=GsonBuilder().create().toJson(favouriteActivity.favouriteSongs)
        editor.putString("FavouriteSongs",jsonString)
        val jsonStringPlaylist = GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
        editor.putString("MusicPlaylist",jsonStringPlaylist)
        editor.apply()




        //for storing favourites using shared preferences
        /*val editor =getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit()
        val jsonStringPlaylist =GsonBuilder().create().toJson(PlaylistActivity.musicPlaylist)
        editor.putString("MusicPlaylist",jsonStringPlaylist)
        editor.apply()*/
    }
    private fun initialAfter(){
        //for retrieving favourites using shared preferences
        favouriteActivity.favouriteSongs = ArrayList()
        val editor =getSharedPreferences("FAVOURITES", MODE_PRIVATE)
        val jsonString=editor.getString("FavouriteSongs",null)
        val typeToken = object :TypeToken<ArrayList<Music>>(){}.type

        if (jsonString != null){
            val data:ArrayList<Music> =GsonBuilder().create().fromJson(jsonString,typeToken)
            favouriteActivity.favouriteSongs.addAll(data)
        }

        PlaylistActivity.musicPlaylist = MusicPlaylist()

        val jsonStringPlaylist =editor.getString("MusicPlaylist",null)
        if (jsonStringPlaylist != null){
            val dataPlaylist:MusicPlaylist =GsonBuilder().create().fromJson(jsonStringPlaylist, MusicPlaylist::class.java)
            PlaylistActivity.musicPlaylist = dataPlaylist
        }

    }

}