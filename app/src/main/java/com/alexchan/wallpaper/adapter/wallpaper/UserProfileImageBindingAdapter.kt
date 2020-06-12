package com.alexchan.wallpaper.adapter.wallpaper

import androidx.databinding.BindingAdapter
import com.alexchan.wallpaper.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("userProfileImage")
fun bindUserProfileImage(view: CircleImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions()
                    .placeholder(R.drawable.ic_baseline_cloud_off)
                    .error(R.drawable.ic_baseline_broken_image))
            .into(view)
    }
}
