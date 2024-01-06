package com.example.mymusicriyas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymusicriyas.databinding.ActivityAboutBinding
import com.example.mymusicriyas.databinding.ActivityFeedbackBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding:ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setTheme(R.style.coolPinkNav)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"
        binding.aboutText.text =aboutText()
        binding.backBtnAB.setOnClickListener {
            finish()
        }
    }
    private fun aboutText():String{
        return "Developed By Riyas Pullur" +
                "\n\n If you want to provide feedback, \n I will love to hear that. \n\n\n" + "Whatsapp : 9072990008"
    }
}