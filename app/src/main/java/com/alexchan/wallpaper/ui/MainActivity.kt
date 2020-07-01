package com.alexchan.wallpaper.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.util.TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set TopToolbar NavigationOnClickListener
        topToolbar.setNavigationOnClickListener{openNavDrawer()}

        // Set TopToolbar OnMenuItemClickListener
        topToolbar.setOnMenuItemClickListener{item: MenuItem? -> onMenuItemClick(item)}

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

    private fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {

                // Handle Search View
                val searchView = item.actionView as SearchView
                searchView.queryHint = getString(R.string.search_photos)
                searchView.isSubmitButtonEnabled = true

                // Hide and UnHide changeDisplayView Menu Item
                searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
                    topToolbar.menu.findItem(R.id.changeDisplayView).isVisible = !hasFocus
                }

                // Handle Search Query
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        searchView.clearFocus()

                        if (!query.isNullOrEmpty()) {
                            searchQuery = query
                            searchStatus = true

                            Toast.makeText(this@MainActivity, "Searching: $query", Toast.LENGTH_LONG).show()
                            val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                            val navController = navHostFragment?.findNavController()
                            navController?.navigate(R.id.wallpaperFragment)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            Toast.makeText(this@MainActivity, "Searching: $newText", Toast.LENGTH_SHORT).show()

                            searchQuery = newText
                            searchStatus = true

                            val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                            val navController = navHostFragment?.findNavController()
                            navController?.navigate(R.id.wallpaperFragment)
                        }
                        return true
                    }
                })
                true
            }
            R.id.listView -> {
                // Handle List View
                //photosGrid.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                photosGrid.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                true
            }
            R.id.gridView -> {
                // Handle Grid View
                //unsplashPhotoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                //unsplashPhotoImageView.requestLayout()
                photosGrid.layoutManager = GridLayoutManager(this, 2)
                true
            }
            R.id.staggeredGridView -> {
                // Handle Staggered View
                photosGrid.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                true
            }
            else -> false
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
        val searchView = topToolbar.menu.findItem(R.id.search).actionView as SearchView
        when (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            true -> {
                Log.d(TAG, "Drawer is Closed")
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            false -> when (!searchView.isIconified) {
                true -> topToolbar.collapseActionView()
                false -> super.onBackPressed()
            }
        }
    }

    companion object {
        var searchQuery : String = ""
        var searchStatus : Boolean = false
    }
}
