package com.alexchan.wallpaper.adapter.wallpaper

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.ui.wallpaper.dashboard.UnsplashApiStatus

@BindingAdapter("unsplashApiStatus")
fun bindStatus(statusImageView: ImageView, status: UnsplashApiStatus?) {
    when (status) {
        UnsplashApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation_api_status)
        }
        UnsplashApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_baseline_cloud_off)
        }
        UnsplashApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
