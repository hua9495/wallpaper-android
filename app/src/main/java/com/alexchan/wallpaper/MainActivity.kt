package com.alexchan.wallpaper

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

//    private val REQUEST_CODE = 100

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
        navigation_view.setNavigationItemSelectedListener(drawerNavListener())
        // Set Checked Item Wallpaper by default
        navigation_view.setCheckedItem(R.id.drawer_Item_Wallpaper)

        // Set BottomNavigationView OnItemClickListener
        bottom_navigation.setOnNavigationItemSelectedListener(bottomNavListener())

        // Default Wallpaper Fragment
        Log.d(TAG, "Wallpaper fragment host created by default")
        createNavigationHost(R.navigation.navigation_host_wallpaper)
    }

    // Open Navigation Drawer
    private fun openNavDrawer() {
        Log.d(TAG, "Drawer is Open")
        drawer_layout.openDrawer(GravityCompat.START)
    }

    // Create Navigation Host
    private fun createNavigationHost(navigationHost: Int) {
        val host = NavHostFragment.create(navigationHost)
        supportFragmentManager.beginTransaction().replace(R.id.mainNavHostFragment, host)
            .setPrimaryNavigationFragment(host).commit()
    }

    // Bottom Navigation Listener
    private fun bottomNavListener() : BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.item_Wallpaper -> {
                    Log.d(TAG, "Bottom Navigation Bar: Wallpaper navigation host created")
                    createNavigationHost(R.navigation.navigation_host_wallpaper)
                    true
                }
                R.id.item_Notification -> {
                    Log.d(TAG, "Bottom Navigation Bar: Notification navigation host created")
                    createNavigationHost(R.navigation.navigation_host_notification)
                    true
                }
                R.id.item_Profile -> {
                    Log.d(TAG, "Bottom Navigation Bar: Profile navigation host created")
                    createNavigationHost(R.navigation.navigation_host_profile)
                    true
                }
                else -> false
            }
        }

    // Drawer Navigation Listener
    private fun drawerNavListener() : NavigationView.OnNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.drawer_Item_Wallpaper -> {
                    Log.d(TAG, "Drawer Item Wallpaper Selected")
                    createNavigationHost(R.navigation.navigation_host_wallpaper)
                }
                R.id.drawer_Item_Notification -> {
                    Log.d(TAG, "Drawer Item Notification Selected")
                    createNavigationHost(R.navigation.navigation_host_notification)
                }
                R.id.drawer_Item_Profile -> {
                    Log.d(TAG, "Drawer Item Profile Selected")
                    createNavigationHost(R.navigation.navigation_host_profile)
                }
            }

            it.isChecked = true
            Log.d(TAG, "Drawer is Closed")
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val photos: ArrayList<UnsplashPhoto>? = data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
        }
    }*/

    // Handle OnClick events
    override fun onClick(v: View?) {
        when (v!!.id) {
            /*R.id.btn_photo_picker -> {

                val intent = Intent(this, UnsplashPickerActivity::class.java)
                startActivityForResult(UnsplashPickerActivity.getStartingIntent(
                    this,
                    isMultipleSelection = true), REQUEST_CODE
                )
            }*/
        }
    }
}
