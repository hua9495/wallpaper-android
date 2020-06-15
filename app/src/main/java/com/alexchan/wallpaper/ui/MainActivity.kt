package com.alexchan.wallpaper.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.util.TAG
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set TopToolbar OnClickListener
        topToolbar.setOnClickListener{openNavDrawer()}

        // Default Wallpaper Navigation Host
        Log.d(TAG, "Wallpaper Navigation host created by default")
        val navController = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
            ?.findNavController()

        if (navController != null) {
            NavigationUI.setupWithNavController(bottomNavigation, navController)
            NavigationUI.setupWithNavController(navigationView, navController)
        }
    }

    // Open Navigation Drawer
    private fun openNavDrawer() {
        updateNavigationDrawerSetCheckedItem()
        Log.d(TAG, "Drawer is Open")
        drawerLayout.openDrawer(GravityCompat.START)
    }

    // Update Navigation Drawer Set Checked Item State
    private fun updateNavigationDrawerSetCheckedItem() {
        Log.d(TAG, supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
            ?.findNavController()?.currentDestination?.id.toString())
        when (supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)?.findNavController()?.currentDestination?.id) {
            R.id.wallpaperFragment -> {
                navigationView.setCheckedItem(R.id.wallpaperFragment)
            }
            R.id.notificationFragment -> {
                navigationView.setCheckedItem(R.id.notificationFragment)
            }
            R.id.profileFragment -> {
                navigationView.setCheckedItem(R.id.profileFragment)
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "Drawer is Closed")
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
