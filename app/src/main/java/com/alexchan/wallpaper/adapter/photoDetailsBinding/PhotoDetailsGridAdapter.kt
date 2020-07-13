package com.alexchan.wallpaper.adapter.photoDetailsBinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexchan.wallpaper.databinding.ItemDetailsGridviewBinding
import com.alexchan.wallpaper.model.unsplash.Photo
import com.alexchan.wallpaper.ui.wallpaper.dashboard.details.WallpaperDetailsFragmentDirections

class PhotoDetailsGridAdapter : ListAdapter<Photo, PhotoDetailsGridAdapter.PhotoDetailsPropertyViewHolder>(
    DiffCallback
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoDetailsPropertyViewHolder {
        return PhotoDetailsPropertyViewHolder(
            ItemDetailsGridviewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoDetailsPropertyViewHolder, position: Int) {
        val photo = getItem(position)
        holder.itemView.setOnLongClickListener {
            it.findNavController().navigate(
                WallpaperDetailsFragmentDirections.actionShowDownloadDetails(photo)
            )
            true
        }
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

    class PhotoDetailsPropertyViewHolder(private var binding: ItemDetailsGridviewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userPhotos: Photo) {
            binding.photoDetails = userPhotos
            binding.executePendingBindings()
        }
    }
}
