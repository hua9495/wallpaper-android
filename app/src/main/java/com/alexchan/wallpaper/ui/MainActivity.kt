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
import kotlinx.android.synthetic.main.fragment_wallpaper.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set TopToolbar OnClickListener
        topToolbar.setNavigationOnClickListener{openNavDrawer()}

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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
        val navController = navHostFragment?.findNavController()
        val navCurrentDestination = navController?.currentDestination?.id
        val count = supportFragmentManager.backStackEntryCount
        Log.d(TAG, count.toString())
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.d(TAG, "Drawer is Closed")
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (viewPager != null && viewPager.currentItem != 1 && navCurrentDestination == R.id.wallpaperFragment) {
            viewPager.setCurrentItem(1, true)
        } else if ((navCurrentDestination != R.id.wallpaperFragment)) {
            navController?.navigate(R.id.wallpaperFragment)
        } else {
            super.onBackPressed()
        }
    }
}
