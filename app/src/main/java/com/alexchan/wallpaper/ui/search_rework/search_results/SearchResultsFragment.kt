package com.alexchan.wallpaper.ui.search_rework.search_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alexchan.wallpaper.adapter.search_results.PhotoSearchResultsGridAdapter
import com.alexchan.wallpaper.databinding.FragmentSearchResultsBinding
import com.alexchan.wallpaper.util.shared_preferences.DisplayViewTypePreferences


class SearchResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSearchResultsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val userSearchQuery = SearchResultsFragmentArgs.fromBundle(requireArguments()).searchQuery
        val viewModelFactory = SearchResultsViewModelFactory(userSearchQuery)
        val searchResultsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchResultsViewModel::class.java)

        binding.searchResultsViewModel = searchResultsViewModel
        binding.photosGrid.adapter =
            PhotoSearchResultsGridAdapter(PhotoSearchResultsGridAdapter.OnClickListener {
                searchResultsViewModel.displayUserCollection(it)
            })
        searchResultsViewModel.navigateToUserCollection.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    SearchResultsFragmentDirections.actionShowSearchedPhotoDetail(it)
                )
                searchResultsViewModel.displayUserCollectionComplete()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.bind<FragmentSearchResultsBinding>(view)

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

        // Disable swipe refresh layout
        binding?.photosGridSwipeRefresh?.isEnabled = false
    }

}
