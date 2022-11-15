package com.codennamdi.ancop_admin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.codennamdi.ancop_admin.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.add_event)
        binding.bottomNavAdmin.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        (R.id.home_menu_admin_id) -> {
            clickedAdminHomeNavBtn()
        }

        (R.id.leaders_menu_admin_id) -> {
            clickedAdminLeadersNavBtn()
        }

        (R.id.library_menu_admin_id) -> {
            clickedAdminLibraryNavBtn()
        }
        else -> false
    }

    private fun clickedAdminHomeNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.admin_fragment_container_view_id, HomeFragmentAdmin())
        }
        title = getString(R.string.add_event)
        return true
    }

    private fun clickedAdminLeadersNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.admin_fragment_container_view_id, LeadersFragmentAdmin())
        }
        title = getString(R.string.add_leaders)
        return true
    }

    private fun clickedAdminLibraryNavBtn(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.admin_fragment_container_view_id, LibraryFragmentAdmin())
        }
        title = getString(R.string.add_books)
        return true
    }
}