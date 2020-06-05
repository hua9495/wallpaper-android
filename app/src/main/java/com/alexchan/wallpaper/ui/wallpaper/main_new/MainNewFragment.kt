package com.alexchan.wallpaper.ui.wallpaper.main_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexchan.wallpaper.adapter.wallpaper.PhotoGridAdapter
import com.alexchan.wallpaper.databinding.FragmentMainNewBinding

class MainNewFragment : Fragment() {

    private val viewModel: MainNewViewModel by lazy {
        ViewModelProvider(this).get(MainNewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainNewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the MainNewViewModel
        binding.mainNewViewModel = viewModel

        binding.photosGrid.adapter = PhotoGridAdapter()

        return binding.root
    }

    /*
    // For testing purposes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Test
        val urlRaw = "https://images.unsplash.com/photo-1505761671935-60b3a7427bad?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEzMjQ3M30"
        val urlFull = "https://images.unsplash.com/photo-1505761671935-60b3a7427bad?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjEzMjQ3M30"
        val urlRegular = "https://images.unsplash.com/photo-1505761671935-60b3a7427bad?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEzMjQ3M30"

        //val imgUri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(this)
            .load(urlRaw)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_baseline_broken_image))
            .into(unsplashPhotoImageView)
    }
    */
}