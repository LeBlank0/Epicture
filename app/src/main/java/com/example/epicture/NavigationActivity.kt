package com.example.epicture

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_navigation.*


class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        val mainpage = mainPage()
        val profilepage = profilePage()
        val favoritepage = favoritePage()
        val searchpage = searchPage()
        val addpage = addPage()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, mainpage)
            commit()
        }
        fab.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, addpage)
                commit()
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.miHome -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, mainpage)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miSearch -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, searchpage)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miFavorite -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, favoritepage)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.miProfile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, profilepage)
                        commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}