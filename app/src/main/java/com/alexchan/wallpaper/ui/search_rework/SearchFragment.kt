package com.alexchan.wallpaper.ui.search_rework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.ui.search_rework.search_results.SearchResultsFragmentDirections
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

        // Set up search toolbar back navigation
        searchToolbar.setNavigationOnClickListener { backToDashboard() }

        // Set up search toolbar to request focus when launch
        setupSearchToolbar()

        // Perform search based on the keyboard soft input search button clicked
        searchTextInputEditText.setOnEditorActionListener { v, actionId, _ ->
            onActionPerform(
                v,
                actionId
            )
        }

        // Handle search query change
        searchTextInputEditText.addTextChangedListener { searchQueryIsChangeAndShowSearchSuggestions() }

        // Handle close button clicked
        searchTextInputLayout.setEndIconOnClickListener { clearSearchQueryAndShowSearchSuggestions() }
    }

    private fun showSoftInputKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(searchTextInputEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftInputKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(searchTextInputEditText.windowToken, 0)
    }

    private fun setupSearchToolbar() {
        searchTextInputEditText.requestFocus()
        showSoftInputKeyboard()
    }

    private fun onActionPerform(v: TextView, actionId: Int): Boolean {
        return when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> {
                // Handle search
                if (!v.text.isNullOrBlank()) {
                    v.clearFocus()
                    showSearchResults(v.text.toString())
                }
                true
            }
            else -> false
        }
    }

    private fun searchQueryIsChangeAndShowSearchSuggestions() {
        if (!searchTextInputEditText.text.isNullOrBlank()) {
            searchQueryIsChangeShowSearchSuggestions()
        }
    }

    private fun clearSearchQueryAndShowSearchSuggestions() {
        // Display Search Suggestions
        if (!searchTextInputEditText.text.isNullOrBlank()) {
            searchTextInputEditText.text?.clear()
            showSoftInputKeyboard()
            searchTextInputEditText.requestFocus()
            searchQueryIsChangeShowSearchSuggestions()
        }
    }

    private fun searchQueryIsChangeShowSearchSuggestions() {
        if (Navigation.findNavController(
                requireActivity(),
                R.id.searchFragmentContainerView
            ).currentDestination?.id != R.id.searchSuggestionsFragment
        ) {
            Navigation.findNavController(
                requireActivity(),
                R.id.searchFragmentContainerView
            )
                .navigate(
                    SearchResultsFragmentDirections.actionShowSearchSuggestions()
                )
        }
    }

    private fun showSearchResults(searchQuery: String) {
        hideSoftInputKeyboard()
        Toast.makeText(requireContext(), "Searching: $searchQuery", Toast.LENGTH_LONG).show()

        Navigation.findNavController(requireActivity(), R.id.searchFragmentContainerView)
            .navigate(SearchSuggestionsFragmentDirections.actionShowSearchedResults(searchQuery))
    }

    private fun backToDashboard() {
        hideSoftInputKeyboard()
        requireActivity().findNavController(R.id.mainNavHostFragment).navigateUp()
    }
}
