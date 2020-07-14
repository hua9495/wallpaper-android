package com.alexchan.wallpaper.ui.wallpaper.dashboard.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.adapter.photoBinding.PhotoGridAdapter
import com.alexchan.wallpaper.databinding.FragmentDashboardBinding
import com.alexchan.wallpaper.ui.mainActivity.MainActivity
import com.alexchan.wallpaper.ui.search.searchActivity.SearchActivity
import com.alexchan.wallpaper.ui.wallpaper.wallpaper.WallpaperFragmentDirections
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

        // Reset Pagination
        MainActivity.paginationStatus = true

        val binding = DataBindingUtil.bind<FragmentDashboardBinding>(view)

        // Use Shared Preference to get user selected display view type
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            DISPLAY_VIEW_TYPE, Context.MODE_PRIVATE)

        // Set Layout Manager Programmatically
        when (sharedPreferences.getInt(DISPLAY_VIEW_TYPE_KEY, 2)) {
            0 -> binding?.photosGrid?.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            1 -> binding?.photosGrid?.layoutManager = GridLayoutManager(requireContext(), 2)
            else -> binding?.photosGrid?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        // Experimental
        binding?.photosGrid?.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Add previous and next button on click listener and set current page number
        if (MainActivity.showPagination) {
            binding?.pageNumberTextView?.apply {
                visibility = View.VISIBLE
                text = MainActivity.pageNumber.toString()
            }
            binding?.previousButton?.apply {
                visibility = View.VISIBLE
                setOnClickListener {displayPreviousPage()}
            }
            binding?.nextButton?.apply {
                visibility = View.VISIBLE
                setOnClickListener {displayNextPage()}
            }
        }

        binding?.photosGridSwipeRefresh?.isEnabled = false
        // Handle Swipe Refresh Layout
        if (MainActivity.showPagination) {
            binding?.photosGridSwipeRefresh?.apply {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setProgressBackgroundColorSchemeResource(R.color.colorOnPrimary)
                        setColorSchemeResources(R.color.colorPrimary)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        setProgressBackgroundColorSchemeResource(R.color.colorPrimary)
                        setColorSchemeResources(R.color.colorOnPrimary)
                    }
                }
                isEnabled = true
                setOnRefreshListener {refreshDashboard()}
            }
        }
    }

    private fun refreshDashboard() {
        // Get current internet connection status
        val connectionManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkManager = connectionManager?.activeNetwork
        val activeNetwork = connectionManager?.getNetworkCapabilities(networkManager)
        // Check if it is connected to mobile data or wifi and have valid connected access
        if (activeNetwork != null) {
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                photosGridSwipeRefresh.isRefreshing = false
                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                val navController = navHostFragment?.findNavController()
                navController?.popBackStack(R.id.wallpaperFragment, true)
                navController?.navigate(R.id.wallpaperFragment)
            } else {
                photosGridSwipeRefresh.isRefreshing = false
                Toast.makeText(requireContext(), requireContext().getString(R.string.no_active_connection), Toast.LENGTH_LONG).show()
            }
        } else {
            photosGridSwipeRefresh.isRefreshing = false
            Toast.makeText(requireContext(), requireContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }
    }

    private fun displayPreviousPage() {
        // Get current internet connection status
        val connectionManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkManager = connectionManager?.activeNetwork
        val activeNetwork = connectionManager?.getNetworkCapabilities(networkManager)
        // Check if it is connected to mobile data or wifi and have valid connected access
        if (activeNetwork != null) {
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                MainActivity.paginationStatus = true
                if (MainActivity.pageNumber > 1) {
                    MainActivity.pageNumber = MainActivity.pageNumber - 1
                    val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                    val navController = navHostFragment?.findNavController()
                    navController?.popBackStack(R.id.wallpaperFragment, true)
                    navController?.navigate(R.id.wallpaperFragment)
                } else {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.you_are_already_on_the_first_page), Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), requireContext().getString(R.string.no_active_connection), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), requireContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }
    }

    private fun displayNextPage() {
        // Get current internet connection status
        val connectionManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkManager = connectionManager?.activeNetwork
        val activeNetwork = connectionManager?.getNetworkCapabilities(networkManager)
        // Check if it is connected to mobile data or wifi and have valid connected access
        if (activeNetwork != null) {
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                MainActivity.paginationStatus = true
                MainActivity.pageNumber = MainActivity.pageNumber + 1
                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                val navController = navHostFragment?.findNavController()
                navController?.popBackStack(R.id.wallpaperFragment, true)
                navController?.navigate(R.id.wallpaperFragment)
            } else {
                Toast.makeText(requireContext(), requireContext().getString(R.string.no_active_connection), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), requireContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }
    }

    private fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {
                MainActivity.paginationStatus = false
                val intent = Intent(requireContext(), SearchActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
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
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            DISPLAY_VIEW_TYPE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(DISPLAY_VIEW_TYPE_KEY, displayViewType)
        editor.apply()
    }

    companion object {
        var DISPLAY_VIEW_TYPE : String = "DISPLAY_VIEW_TYPE"
        var DISPLAY_VIEW_TYPE_KEY : String = "display_view_type_key"
    }
}
