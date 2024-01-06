package com.example.mymusicriyas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymusicriyas.databinding.ActivityFeedbackBinding
import com.example.mymusicriyas.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"
    }
}