package com.alexchan.wallpaper.ui.wallpaper.dashboard.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.adapter.wallpaper.PhotoDetailsGridAdapter
import com.alexchan.wallpaper.databinding.FragmentWallpaperDetailsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_wallpaper_details.*

class WallpaperDetailsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentWallpaperDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val userPhotoCollection = WallpaperDetailsFragmentArgs.fromBundle(requireArguments()).selectedPhoto
        val viewModelFactory = WallpaperDetailsViewModelFactory(userPhotoCollection, application)
        val wallpaperDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(WallpaperDetailsViewModel::class.java)
        binding.wallpaperDetailsViewModel = wallpaperDetailsViewModel
        binding.photoDetailsGrid.adapter = PhotoDetailsGridAdapter()

        // Hide Top Toolbar
        requireActivity().topToolbar.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set TitleTopToolbar OnNavigationClickListener navigate back to Wallpaper Fragment
        requireActivity().titleTopToolbar.setNavigationOnClickListener {
            it.findNavController().popBackStack(R.id.wallpaperFragment, true)
            it.findNavController().navigate(R.id.wallpaperFragment)
        }
    }
}
