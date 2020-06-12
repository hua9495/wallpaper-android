package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alexchan.wallpaper.adapter.wallpaper.PhotoGridAdapter
import com.alexchan.wallpaper.databinding.FragmentDashboardBinding
import com.alexchan.wallpaper.ui.wallpaper.WallpaperFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*

class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDashboardBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the MainNewViewModel
        binding.dashboardViewModel = dashboardViewModel

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            dashboardViewModel.displayUserCollection(it)
        })
        dashboardViewModel.navigateToUserCollection.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    WallpaperFragmentDirections.actionShowDetail(it))
                dashboardViewModel.displayUserCollectionComplete()
            }
        })

        // Show Top Toolbar
        requireActivity().topToolbar.visibility = View.VISIBLE

        return binding.root
    }
}
