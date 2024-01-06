package com.example.mymusicriyas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicriyas.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySelectionBinding
    private lateinit var adapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySelectionBinding.inflate(layoutInflater)
        setTheme(R.style.coolPink)
        setContentView(binding.root)


        binding.selectionRV.setItemViewCacheSize(10)
        binding.selectionRV.setHasFixedSize(true)
        binding.selectionRV.layoutManager = LinearLayoutManager(this)

        adapter = MusicAdapter(this,MainActivity.MusicListMA, selectionActivity = true)
        binding.selectionRV.adapter = adapter
        binding.backBtnSA.setOnClickListener {
            finish()
        }
        //forsearch
        binding.searchViewSA.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query : String?): Boolean =true

            override fun onQueryTextChange(newText : String?): Boolean {
                MainActivity.musicListSearch =ArrayList()

                if (newText!=null){
                    var userInput=newText.lowercase()
                    for (song in MainActivity.MusicListMA)
                        if (song.title.lowercase().contains(userInput))
                            MainActivity.musicListSearch.add(song)

                    MainActivity.search =true
                    adapter.updateMusicList(searchList = MainActivity.musicListSearch)
                }
                return true
            }
        })
    }
}