package com.codennamdi.ancopapp.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.ActivityMainBinding
import com.codennamdi.ancopapp.fragments.ChapelineFragment
import com.codennamdi.ancopapp.fragments.HomeFragment
import com.codennamdi.ancopapp.fragments.HymnFrament
import com.codennamdi.ancopapp.fragments.LeadersFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        (R.id.profile) -> {
            Toast.makeText(this, "Clicked Profile", Toast.LENGTH_LONG).show()
            true
        }

        (R.id.settings) -> {
            Toast.makeText(this, "Clicked Settings", Toast.LENGTH_LONG).show()
            true
        }

        (R.id.sign_out) -> {
            Toast.makeText(this, "Clicked Sign out", Toast.LENGTH_LONG).show()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        (R.id.home_menu_id) -> {
            clickedHomeNavBtn()
        }

        (R.id.Hymns) -> {
            clickedHymnsNavBtn()
        }

        (R.id.leaders) -> {
            clickedLeadersNavBtn()
        }

        (R.id.library) -> {
            clickedLibraryNavBtn()
        }

        (R.id.chapeline_bio) -> {
            clickedChapelineBioNavBtn()
        }

        else -> false
    }

    private fun clickedHomeNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view_id, HomeFragment())
        }
        return true
    }

    private fun clickedHymnsNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view_id, HymnFrament())
        }
        return true
    }

    private fun clickedLeadersNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view_id, LeadersFragment())
        }
        return true
    }

    private fun clickedLibraryNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view_id, HymnFrament())
        }
        return true
    }

    private fun clickedChapelineBioNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view_id, ChapelineFragment())
        }
        return true
    }
}