package com.codennamdi.ancop_admin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codennamdi.ancop_admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.textViewCreateOne.setOnClickListener {
            startActivity(Intent(this@MainActivity, AdminRegisterActivity::class.java))
        }

        binding.btnLoginAdmin.setOnClickListener {
            Toast.makeText(this@MainActivity, "Logged in", Toast.LENGTH_LONG).show()
        }
    }
}