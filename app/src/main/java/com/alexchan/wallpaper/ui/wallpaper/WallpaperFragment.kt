package com.alexchan.wallpaper.ui.wallpaper

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.ui.wallpaper.dashboard.DashboardFragment
import com.alexchan.wallpaper.ui.wallpaper.follow.FollowFragment
import com.alexchan.wallpaper.ui.wallpaper.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_wallpaper.*

class WallpaperFragment : Fragment(R.layout.fragment_wallpaper) {

    private lateinit var adapter: WallpaperPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WallpaperPagerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = adapter
        viewPager.currentItem = 1
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class WallpaperPagerAdapter :
        FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments = arrayOf(HomeFragment(), DashboardFragment(), FollowFragment())

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.home)
                1 -> getString(R.string.dashboard)
                else -> getString(R.string.follow)
            }
        }
    }
}
