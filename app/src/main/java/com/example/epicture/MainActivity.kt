package com.example.epicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class client {
    companion object {
        const val clientId = "773f624284c450e"
        const val clientSecret = "e6a1f7c260644ce5fe38f449ef8134ba82afd106"
        var accessToken: String? = ""
        var refreshToken: String? = ""
        var accountUsername: String? = ""
        var isConnected: Boolean = false

        fun logoutClient() {
            accessToken = ""
            refreshToken = ""
            accountUsername = ""
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}