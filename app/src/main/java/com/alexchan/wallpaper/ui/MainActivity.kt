package com.alexchan.wallpaper.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import com.alexchan.wallpaper.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Change to saveButton or photoPickerButton

    // For Log statement
    companion object {
        private val TAG: String?= MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set TopToolbar OnClickListener
        topToolbar.setOnClickListener{openNavDrawer()}

        // Set NavigationView Drawer OnItemCLickListener
        navigationView.setNavigationItemSelectedListener(drawerNavListener())
        // Set Checked Item Wallpaper by default
        navigationView.setCheckedItem(R.id.drawer_wallpaper)

        // Set BottomNavigationView OnItemClickListener
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavListener())

        // Default Wallpaper Fragment
        Log.d(TAG, "Wallpaper fragment host created by default")
        createNavHost(R.navigation.wallpaper)
    }

    // Open Navigation Drawer
    private fun openNavDrawer() {
        Log.d(TAG, "Drawer is Open")
        drawerLayout.openDrawer(GravityCompat.START)
    }

    // Create Navigation Host
    private fun createNavHost(navHost: Int) {
        val host = NavHostFragment.create(navHost)
        supportFragmentManager.beginTransaction().replace(R.id.mainNavHostFragment, host)
            .setPrimaryNavigationFragment(host).commit()
    }

    // Bottom Navigation Listener
    private fun bottomNavListener() : BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.wallpaper -> {
                    Log.d(TAG, "Bottom Navigation Bar: Wallpaper navigation host created")
                    createNavHost(R.navigation.wallpaper)
                    true
                }
                R.id.notification -> {
                    Log.d(TAG, "Bottom Navigation Bar: Notification navigation host created")
                    createNavHost(R.navigation.notification)
                    true
                }
                R.id.profile -> {
                    Log.d(TAG, "Bottom Navigation Bar: Profile navigation host created")
                    createNavHost(R.navigation.profile)
                    true
                }
                else -> false
            }
        }

    // Drawer Navigation Listener
    private fun drawerNavListener() : NavigationView.OnNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.drawer_wallpaper -> {
                    Log.d(TAG, "Drawer Item Wallpaper Selected")
                    createNavHost(R.navigation.wallpaper)
                }
                R.id.drawer_notification -> {
                    Log.d(TAG, "Drawer Item Notification Selected")
                    createNavHost(R.navigation.notification)
                }
                R.id.drawer_profile -> {
                    Log.d(TAG, "Drawer Item Profile Selected")
                    createNavHost(R.navigation.profile)
                }
            }

            it.isChecked = true
            Log.d(TAG, "Drawer is Closed")
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
}
