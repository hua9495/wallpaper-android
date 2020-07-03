package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import com.alexchan.wallpaper.ui.wallpaper.WallpaperFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*

class DashboardFragment : Fragment() {

    // Hold a Reference to PhotosGrid Recylerview
    val photosGridRecylerView by lazy { requireActivity().findViewById<RecyclerView>(R.id.photosGrid) }

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
        val binding = DataBindingUtil.bind<FragmentDashboardBinding>(view)

        // Set Layout Manager Programmatically
        binding?.photosGrid?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Experimental
        binding?.photosGrid?.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {

                // Handle Search View
                val searchView = item.actionView as SearchView
                searchView.queryHint = getString(R.string.search_photos)
                searchView.isSubmitButtonEnabled = true

                // Hide and UnHide changeDisplayView Menu Item
                searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
                    requireActivity().topToolbar.menu.findItem(R.id.changeDisplayView).isVisible = !hasFocus
                }

                // Handle Search Query
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        searchView.clearFocus()

                        if (!query.isNullOrEmpty()) {
                            Toast.makeText(activity, "Searching: $query", Toast.LENGTH_LONG).show()
                            MainActivity.searchQuery = query
                            MainActivity.searchStatus = true
                        } else {
                            MainActivity.searchQuery = ""
                            MainActivity.searchStatus = false
                        }

                        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                        val navController = navHostFragment?.findNavController()
                        navController?.popBackStack(R.id.wallpaperFragment, true)
                        navController?.navigate(R.id.wallpaperFragment)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
                true
            }
            R.id.listView -> {
                // Handle List View
                //photosGrid.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                photosGridRecylerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                true
            }
            R.id.gridView -> {
                // Handle Grid View
                //unsplashPhotoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                //unsplashPhotoImageView.requestLayout()
                photosGridRecylerView.layoutManager = GridLayoutManager(requireContext(), 2)
                true
            }
            R.id.staggeredGridView -> {
                // Handle Staggered View
                photosGridRecylerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                true
            }
            else -> false
        }
    }
}
