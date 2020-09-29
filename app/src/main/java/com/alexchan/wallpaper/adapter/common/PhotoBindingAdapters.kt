package com.alexchan.wallpaper.adapter.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.alexchan.wallpaper.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_baseline_broken_image)
            )
            .into(imgView)
    }
}
