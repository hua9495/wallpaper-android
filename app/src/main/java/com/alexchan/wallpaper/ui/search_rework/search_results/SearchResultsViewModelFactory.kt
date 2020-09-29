package com.alexchan.wallpaper.ui.search_rework.search_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchResultsViewModelFactory(private val userSearchQuery: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultsViewModel::class.java)) {
            return SearchResultsViewModel(userSearchQuery) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
