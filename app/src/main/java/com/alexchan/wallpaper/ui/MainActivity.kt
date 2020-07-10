package com.alexchan.wallpaper.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.util.TAG
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set TopToolbar NavigationOnClickListener
        //topToolbar.setNavigationOnClickListener{openNavDrawer()}

        // To disable on swipe open navigation drawer gesture
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START)

        // Update On Swipe Open Navigation Drawer Set Checked Item State
        updateOnSwipeOpenNavDrawer()

        // Default Wallpaper Navigation Host
        Log.d(TAG, "Wallpaper Navigation host created by default")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
        val navController = navHostFragment?.findNavController()

        // Set up Bottom Navigation and Navigation Drawer with navController
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

    // Update on Swipe Open Navigation Drawer Set Checked Item State
    private fun updateOnSwipeOpenNavDrawer() {
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                updateNavigationDrawerSetCheckedItem()
            }
        })
    }

    // Update Navigation Drawer Set Checked Item State
    private fun updateNavigationDrawerSetCheckedItem() {
        Log.d(TAG, supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
            ?.findNavController()?.currentDestination?.id.toString())
        when (supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)?.findNavController()?.currentDestination?.id) {
            R.id.wallpaperFragment -> navigationView.setCheckedItem(R.id.wallpaperFragment)
            R.id.notificationFragment -> navigationView.setCheckedItem(R.id.notificationFragment)
            R.id.profileFragment -> navigationView.setCheckedItem(R.id.profileFragment)
        }
    }

    override fun onBackPressed() {
        when (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            true -> {
                Log.d(TAG, "Drawer is Closed")
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            false -> super.onBackPressed()
        }
    }

    companion object {
        var searchQuery : String = ""
        var searchStatus : Boolean = false
        var pageNumber : Int = 1
        var paginationStatus : Boolean = false
        var showPagination : Boolean = true
    }
}
