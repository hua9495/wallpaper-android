package com.alexchan.wallpaper.ui.wallpaper.wallpaper

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.ui.wallpaper.dashboard.dashboard.DashboardFragment
import com.alexchan.wallpaper.ui.wallpaper.follow.FollowFragment
import com.alexchan.wallpaper.ui.wallpaper.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_wallpaper.*

class WallpaperFragment : Fragment(R.layout.fragment_wallpaper) {

    private lateinit var viewPagerAdapter: WallpaperPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter =
            WallpaperPagerAdapter(
                this
            )
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = WallpaperPagerAdapter.FRAGMENT_OFF_SCREEN_PAGE_LIMIT as Int
            setCurrentItem(1, false)
        }

        TabLayoutMediator(tabLayout, viewPager) { currentTab, currentPosition ->
            currentTab.text = when (currentPosition) {
                WallpaperPagerAdapter.HOME_FRAGMENT_SCREEN_POSITION -> getString(R.string.home)
                WallpaperPagerAdapter.DASHBOARD_FRAGMENT_SCREEN_POSITION -> getString(R.string.dashboard)
                else -> getString(R.string.follow)
            }
        }.attach()
    }

    internal class WallpaperPagerAdapter(wallpaperFragment: WallpaperFragment) :
        FragmentStateAdapter(wallpaperFragment) {

        override fun createFragment(position: Int): Fragment = when (position) {
            HOME_FRAGMENT_SCREEN_POSITION -> HomeFragment()
            DASHBOARD_FRAGMENT_SCREEN_POSITION -> DashboardFragment()
            FOLLOW_FRAGMENT_SCREEN_POSITION -> FollowFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }


        override fun getItemCount(): Int =
            TOTAL_FRAGMENT_SCREEN

        companion object {
            internal const val FRAGMENT_OFF_SCREEN_PAGE_LIMIT = 3

            internal const val TOTAL_FRAGMENT_SCREEN = 3

            internal const val HOME_FRAGMENT_SCREEN_POSITION = 0
            internal const val DASHBOARD_FRAGMENT_SCREEN_POSITION = 1
            internal const val FOLLOW_FRAGMENT_SCREEN_POSITION = 2
        }
    }
}
