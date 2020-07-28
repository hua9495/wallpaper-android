package com.alexchan.wallpaper.ui.search_rework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.alexchan.wallpaper.R
import kotlinx.android.synthetic.main.activity_main.*
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
        requireActivity().bottomNavigation.isGone = true

        searchTextInputEditText.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(searchTextInputEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun backToDashboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(searchTextInputEditText.windowToken, 0)
        requireActivity().findNavController(R.id.mainNavHostFragment).navigateUp()
    }
}
