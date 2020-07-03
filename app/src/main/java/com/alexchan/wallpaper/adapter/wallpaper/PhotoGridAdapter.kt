package com.alexchan.wallpaper.adapter.wallpaper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexchan.wallpaper.databinding.ItemGridviewBinding
import com.alexchan.wallpaper.model.unsplash.Photo

class PhotoGridAdapter(private val onClickListener: OnClickListener) : ListAdapter<Photo, PhotoGridAdapter.PhotoPropertyViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoGridAdapter.PhotoPropertyViewHolder {
        return PhotoPropertyViewHolder(ItemGridviewBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.PhotoPropertyViewHolder, position: Int) {
        val photo = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(photo) }
        holder.bind(photo)
    }

    /* When 1 image source change in position 1 (any position)
    Without DiffCallback
    It will reload the whole Recycler View
    With DiffCallback
    It will only reload the position of that Image View without reloading the whole Recycler View */
    companion object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class PhotoPropertyViewHolder(private var binding: ItemGridviewBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photoProperty = photo
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (userPhoto: Photo) -> Unit) {
        fun onClick(userPhoto: Photo) = clickListener(userPhoto)
    }
}
