package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.content.Context
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.adapter.dashboard.PhotoGridAdapter
import com.alexchan.wallpaper.databinding.FragmentDashboardBinding
import com.alexchan.wallpaper.ui.wallpaper.WallpaperFragmentDirections
import com.alexchan.wallpaper.util.shared_preferences.DisplayViewTypePreferences
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_wallpaper.*


class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    WallpaperFragmentDirections.actionShowDetail(it)
                )
                dashboardViewModel.displayUserCollectionComplete()
            }
        })

        // Set TopToolbar OnMenuItemClickListener -> (Change to Floating Action Button)
        requireActivity().topToolbar.setOnMenuItemClickListener { item: MenuItem? ->
            onMenuItemClick(
                item
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.bind<FragmentDashboardBinding>(view)

        // Use Shared Preference to get user selected display view type
        val displayViewTypePreferences = DisplayViewTypePreferences(requireContext())
        // Set Layout Manager Programmatically based on the user display view type preferences
        // Default is 1, which means StaggeredGridLayoutManager is used by default
        when (displayViewTypePreferences.getUserSelectionDisplayViewType()) {
            0 -> binding?.photosGrid?.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            else -> binding?.photosGrid?.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        // Experimental
        binding?.photosGrid?.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Handle Swipe Refresh Layout
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
            setOnRefreshListener { refreshDashboard() }
        }
    }

    private fun refreshDashboard() {
        // Get current internet connection status
        val connectionManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkManager = connectionManager?.activeNetwork
        val activeNetwork = connectionManager?.getNetworkCapabilities(networkManager)
        // Check if it is connected to mobile data or wifi and have valid connected access
        if (activeNetwork != null) {
            if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            ) {
                photosGridSwipeRefresh.isRefreshing = false
                val navHostFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
                val navController = navHostFragment?.findNavController()
                navController?.popBackStack(R.id.wallpaperFragment, true)
                navController?.navigate(R.id.wallpaperFragment)
            } else {
                photosGridSwipeRefresh.isRefreshing = false
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.no_active_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            photosGridSwipeRefresh.isRefreshing = false
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.no_internet_connection),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /*private fun displayPreviousPage() {
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
    }*/

    private fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {
                requireActivity().findNavController(R.id.mainNavHostFragment)
                    .navigate(R.id.searchFragment)
                true
            }
            // Change to Floating Action Button (FAB)
            /*R.id.listView -> {
                // Handle List View
                //photosGrid.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                requireActivity().photosGrid.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                getUserSelectedDisplayViewType(0)
                true
            }
            R.id.staggeredGridView -> {
                // Handle Staggered View
                requireActivity().photosGrid.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                getUserSelectedDisplayViewType(1)
                true
            }*/
            else -> false
        }
    }

}
