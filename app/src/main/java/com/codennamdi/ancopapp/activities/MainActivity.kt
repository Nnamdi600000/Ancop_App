package com.codennamdi.ancopapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}