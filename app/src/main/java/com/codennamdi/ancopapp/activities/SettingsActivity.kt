package com.codennamdi.ancopapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codennamdi.ancopapp.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title = getString(R.string.settings)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }
}