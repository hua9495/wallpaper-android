package com.alexchan.wallpaper.adapter.wallpaper

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexchan.wallpaper.model.unsplash.Photo

@BindingAdapter("listDataDetails")
fun bindDetailsRecyclerView(recyclerView: RecyclerView,
                            data: List<Photo>?) {
    val adapter = recyclerView.adapter as PhotoDetailsGridAdapter
    adapter.submitList(data)
}
