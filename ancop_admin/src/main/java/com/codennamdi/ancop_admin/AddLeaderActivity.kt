package com.codennamdi.ancop_admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codennamdi.ancop_admin.databinding.ActivityAddLeaderBinding

class AddLeaderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddLeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}