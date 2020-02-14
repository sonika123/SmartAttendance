package com.sonika.smartattendance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            openLoginActivity()
        }, 2000)
    }

    /*open login page*/
    private fun openLoginActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}