package com.codennamdi.ancopapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codennamdi.ancopapp.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnLoginGetStarted.setOnClickListener {
            val intent = Intent(this@GetStartedActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterGetStarted.setOnClickListener {
            val intent = Intent(this@GetStartedActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}