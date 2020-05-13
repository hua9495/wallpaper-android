package com.alexchan.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.alexchan.wallpaper.databinding.FragmentWallpaperBinding


/**
 * A simple [Fragment] subclass.
 */
class WallpaperFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentWallpaperBinding>(inflater,
            R.layout.fragment_wallpaper, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

}
