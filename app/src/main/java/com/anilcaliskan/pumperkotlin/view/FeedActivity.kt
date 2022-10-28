package com.anilcaliskan.pumperkotlin.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.anilcaliskan.pumperkotlin.R
import com.anilcaliskan.pumperkotlin.databinding.ActivityFeedBinding
import com.google.firebase.auth.FirebaseAuth

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var preference : SharedPreferences

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preference = this.getSharedPreferences("com.anilcaliskan.pumperkotlin", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val pumperFragment = PumperFragment()
        fragmentManager.beginTransaction().add(R.id.frameLayout, pumperFragment).commit()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pumper_menu -> {
                val pumperFragment = PumperFragment()
                fragmentManager.beginTransaction().add(R.id.frameLayout, pumperFragment).commit()
            }
            R.id.remove_coin_menu -> {
                val removeCoinFragment = RemoveCoinFragment()
                fragmentManager.beginTransaction().replace(R.id.frameLayout, removeCoinFragment).commit()
            }
            R.id.add_coin_menu -> {
                val addCoinFragment = AddCoinFragment()
                fragmentManager.beginTransaction().replace(R.id.frameLayout, addCoinFragment).commit()
            }
            R.id.close_notification_menu -> {

            }
            R.id.change_percentage_menu -> {

            }
            else -> {
                preference.edit().putBoolean("rememberMe", false).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                auth.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}