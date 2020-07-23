package com.alexchan.wallpaper.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.util.TAG
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Default Wallpaper Navigation Host
        Log.d(TAG, "Wallpaper Navigation host created by default")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
        val navController = navHostFragment?.findNavController()

        // Set up Bottom Navigation and Navigation Drawer with navController
        if (navController != null) {
            NavigationUI.setupWithNavController(bottomNavigation, navController)
        }
    }
}
