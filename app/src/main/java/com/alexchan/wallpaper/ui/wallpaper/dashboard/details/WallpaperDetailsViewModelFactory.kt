package com.alexchan.wallpaper.ui.wallpaper.dashboard.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexchan.wallpaper.model.unsplash.Photo

class WallpaperDetailsViewModelFactory(
    private val userPhoto: Photo,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallpaperDetailsViewModel::class.java)) {
            return WallpaperDetailsViewModel(userPhoto, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
