package com.example.epicture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button_login.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=773f624284c450e&response_type=token&state=APPLICATION_STATE"))
            startActivity(openURL)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith("epicture.project.com://callback")) {
            if (uri.getQueryParameter("error") == null) {
                val newUri = Uri.parse(uri.toString().replace('#', '&'))
                client.accessToken = newUri.getQueryParameter("access_token")
                client.refreshToken = newUri.getQueryParameter("refresh_token")
                client.accountUsername = newUri.getQueryParameter("account_username")
                client.isConnected = true
                val myIntent = Intent(this, NavigationActivity::class.java)
                startActivity(myIntent)
                finish()
            }
        }
    }
}