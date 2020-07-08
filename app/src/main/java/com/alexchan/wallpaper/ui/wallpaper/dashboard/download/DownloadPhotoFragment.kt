package com.alexchan.wallpaper.ui.wallpaper.dashboard.download

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexchan.wallpaper.databinding.FragmentDownloadPhotoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DownloadPhotoFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentDownloadPhotoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Bind data from bundle to layout data variable
        binding.photoModel = DownloadPhotoFragmentArgs.fromBundle(requireArguments()).userSelectedPhoto

        // Handle download photo button on click listener
        binding.downloadPhotoButton.setOnClickListener {downloadPhoto()}

        return binding.root
    }

    private fun downloadPhoto() {
        Log.d("Main", "Download button is Clicked!")
    }

}
