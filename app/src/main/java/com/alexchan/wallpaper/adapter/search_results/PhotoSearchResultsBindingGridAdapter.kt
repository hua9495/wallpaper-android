package com.alexchan.wallpaper.adapter.search_results

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexchan.wallpaper.model.unsplash.Photo

@BindingAdapter("listSearchResultsData")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Photo>?
) {
    val adapter = recyclerView.adapter as PhotoSearchResultsGridAdapter
    adapter.submitList(data)
}
