package com.alexchan.wallpaper.ui.search_rework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.ui.search_rework.search_suggestions.SearchSuggestionsFragmentDirections
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchToolbar.setNavigationOnClickListener { backToDashboard() }

        // Hide Bottom Navigation in Search Fragment
        //requireActivity().bottomNavigation.isGone = true

        searchTextInputEditText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(searchTextInputEditText, InputMethodManager.SHOW_IMPLICIT)

        searchTextInputEditText.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    // Handle search
                    v.clearFocus()
                    showSearchResults(v.text.toString())
                    true
                }
                else -> false
            }
        }

        searchTextInputEditText.addTextChangedListener {
            Navigation.findNavController(requireActivity(), R.id.searchFragmentContainerView)
                .navigateUp()
        }

        searchTextInputLayout.setEndIconOnClickListener {
            // Display Search Suggestions
            if (!searchTextInputEditText.text.isNullOrBlank()) {
                searchTextInputEditText.text?.clear()
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(searchTextInputEditText, InputMethodManager.SHOW_IMPLICIT)
            }
            searchTextInputEditText.requestFocus()
            Navigation.findNavController(requireActivity(), R.id.searchFragmentContainerView)
                .navigateUp()
        }
    }

    private fun showSearchResults(searchQuery: String) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(searchTextInputEditText.windowToken, 0)
        Toast.makeText(requireContext(), "Searching: $searchQuery", Toast.LENGTH_LONG).show()

        Navigation.findNavController(requireActivity(), R.id.searchFragmentContainerView)
            .navigate(SearchSuggestionsFragmentDirections.actionShowSearchedResults(searchQuery))
    }

    private fun backToDashboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(searchTextInputEditText.windowToken, 0)
        requireActivity().findNavController(R.id.mainNavHostFragment).navigateUp()
    }
}
