package com.alexchan.wallpaper.adapter.common

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
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
            //statusImageView.visibility = View.VISIBLE
            //statusImageView.setImageResource(R.drawable.ic_baseline_cloud_off)
            statusImageView.visibility = View.GONE
        }
        UnsplashApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("unsplashApiStatusWithLottie")
fun bindStatusWithLottie(statusLottieAnimationView: LottieAnimationView, status: UnsplashApiStatus?) {
    when (status) {
        UnsplashApiStatus.LOADING -> {
            statusLottieAnimationView.visibility = View.GONE
        }
        UnsplashApiStatus.ERROR -> {
            statusLottieAnimationView.visibility = View.VISIBLE
        }
        UnsplashApiStatus.DONE -> {
            statusLottieAnimationView.visibility = View.GONE
        }
    }
}
