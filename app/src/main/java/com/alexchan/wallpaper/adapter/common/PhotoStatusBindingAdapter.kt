package com.alexchan.wallpaper.adapter.common

import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.ui.wallpaper.dashboard.UnsplashApiStatus

@BindingAdapter("unsplashApiStatus")
fun bindStatus(statusImageView: ImageView, status: UnsplashApiStatus?) {
    when (status) {
        UnsplashApiStatus.LOADING -> {
            statusImageView.isVisible = true
            statusImageView.setImageResource(R.drawable.loading_animation_api_status)
        }
        UnsplashApiStatus.ERROR -> {
            //statusImageView.visibility = View.VISIBLE
            //statusImageView.setImageResource(R.drawable.ic_baseline_cloud_off)
            statusImageView.isGone = true
        }
        UnsplashApiStatus.DONE -> {
            statusImageView.isGone = true
        }
    }
}

@BindingAdapter("unsplashApiStatusWithLottie")
fun bindStatusWithLottie(
    statusLottieAnimationView: LottieAnimationView,
    status: UnsplashApiStatus?
) {
    when (status) {
        UnsplashApiStatus.LOADING -> {
            statusLottieAnimationView.isGone = true
        }
        UnsplashApiStatus.ERROR -> {
            statusLottieAnimationView.isVisible = true
        }
        UnsplashApiStatus.DONE -> {
            statusLottieAnimationView.isGone = true
        }
    }
}
