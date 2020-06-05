package com.alexchan.wallpaper.adapter.wallpaper

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.model.unsplash.Photo
import com.alexchan.wallpaper.ui.wallpaper.main_new.UnsplashApiStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_baseline_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<Photo>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("unsplashApiStatus")
fun bindStatus(statusImageView: ImageView, status: UnsplashApiStatus?) {
    when (status) {
        UnsplashApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
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