package com.codennamdi.ancopapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.firebase.FireStoreClass

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUserId = FireStoreClass().getCurrentUserId()
            if (currentUserId.isNotBlank()) {
                startActivity(Intent(this@SplashscreenActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashscreenActivity, GetStartedActivity::class.java))
                finish()
            }
        }, 2500)
    }
}