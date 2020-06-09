package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexchan.wallpaper.adapter.wallpaper.PhotoGridAdapter
import com.alexchan.wallpaper.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private val viewModel: MainNewViewModel by lazy {
        ViewModelProvider(this).get(MainNewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDashboardBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the MainNewViewModel
        binding.mainNewViewModel = viewModel

        binding.photosGrid.adapter = PhotoGridAdapter()

        return binding.root
    }
}
