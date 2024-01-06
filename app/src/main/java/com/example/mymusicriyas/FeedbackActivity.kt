package com.example.mymusicriyas

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.se.omapi.Session
import android.widget.Toast
import com.example.mymusicriyas.databinding.ActivityFeedbackBinding
import java.lang.Exception
import java.net.Authenticator
import java.net.PasswordAuthentication


import java.util.*
import javax.mail.*
import javax.mail.Session.getInstance
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class FeedbackActivity : AppCompatActivity() {
    lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Feedback"


        binding.sendFA.setOnClickListener {
          /*  val feedbackMs=binding.feedbackMsgFA.text.toString() + "\n" +binding.emailFA.text.toString()
            val subject=binding.topicFA.text.toString()

            val userName="riyaspullurofficial@gmail.com"
            val pass="miyamolravamol"

            val cm=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (feedbackMs.isNotEmpty() && subject.isNotEmpty() && (cm.activeNetworkInfo?.isConnectedOrConnecting==true)){

                Thread{
                    try {
                        val properties=Properties()
                        properties["mail.smtp.auth"] ="true"
                        properties["mail.smtp.starttls.enable"] ="true"
                        properties["mail.smtp.host"] ="smtp.gamil.com"
                        properties["mail.smtp.port"] ="587"
                        val session=Session.getInstance(properties,object :Authenticator(){
                            override fun getPasswordAuthentication(): PasswordAuthentication {
                                return super.getPasswordAuthentication(userName,pass)
                            }

                        })
                        val mail=MimeMessage(session)
                        mail.subject=subject
                        mail.setText(feedbackMs)
                        mail.setFrom(InternetAddress(userName))
                        mail.setRecipients(javax.mail.Message.RecipientType.TO,InternetAddress.parse(userName))
                        Transport.send(mail)

                    }
                    catch (e:Exception) {Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()}
                }.start()*/
                Toast.makeText(this,"Thank you...!!",Toast.LENGTH_SHORT).show()
                finish()
            /*
            }else{
                Toast.makeText(this,"Some thing wrong...!!",Toast.LENGTH_SHORT).show()
            }*/


        }
        binding.backFA.setOnClickListener { finish() }
    }
}