package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.adapter.wallpaper.PhotoGridAdapter
import com.alexchan.wallpaper.databinding.FragmentDashboardBinding
import com.alexchan.wallpaper.ui.MainActivity
import com.alexchan.wallpaper.ui.search.SearchActivity
import com.alexchan.wallpaper.ui.wallpaper.WallpaperFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDashboardBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the DashboardViewModel
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

        // Set TopToolbar OnMenuItemClickListener
        requireActivity().topToolbar.setOnMenuItemClickListener{item: MenuItem? -> onMenuItemClick(item)}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reset Search Query
        MainActivity.searchQuery = ""
        MainActivity.searchStatus = false

        val binding = DataBindingUtil.bind<FragmentDashboardBinding>(view)

        // Use Shared Preference to get user selected display view type
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(DISPLAY_VIEW_TYPE, Context.MODE_PRIVATE)

        // Set Layout Manager Programmatically
        when (sharedPreferences.getInt(DISPLAY_VIEW_TYPE_KEY, 2)) {
            0 -> binding?.photosGrid?.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            1 -> binding?.photosGrid?.layoutManager = GridLayoutManager(requireContext(), 2)
            else -> binding?.photosGrid?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        // Experimental
        binding?.photosGrid?.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {
                val intent = Intent(requireContext(), SearchActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.listView -> {
                // Handle List View
                //photosGrid.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                requireActivity().photosGrid.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                getUserSelectedDisplayViewType(0)
                true
            }
            R.id.gridView -> {
                // Handle Grid View
                //unsplashPhotoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                //unsplashPhotoImageView.requestLayout()
                requireActivity().photosGrid.layoutManager = GridLayoutManager(requireContext(), 2)
                getUserSelectedDisplayViewType(1)
                true
            }
            R.id.staggeredGridView -> {
                // Handle Staggered View
                requireActivity().photosGrid.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                getUserSelectedDisplayViewType(2)
                true
            }
            else -> false
        }
    }

    // Save user selection for display view type
    private fun getUserSelectedDisplayViewType(displayViewType: Int) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(DISPLAY_VIEW_TYPE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(DISPLAY_VIEW_TYPE_KEY, displayViewType)
        editor.apply()
    }

    companion object {
        var DISPLAY_VIEW_TYPE : String = "DISPLAY_VIEW_TYPE"
        var DISPLAY_VIEW_TYPE_KEY : String = "display_view_type_key"
    }
}
