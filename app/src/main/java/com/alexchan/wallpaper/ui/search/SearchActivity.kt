package com.alexchan.wallpaper.ui.search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.database.SearchSuggestionProvider
import com.alexchan.wallpaper.ui.search.search_results.SearchResultsActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Set action bar background color -> Replace with Dark Theme support
        //supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(applicationContext, R.color.colorPrimary)))

        // Allow the user to clear recent search history
        clearSearchHistoryButton.setOnClickListener { clearSearchHistoryConfirmation() }
    }

    private fun clearSearchHistoryConfirmation() {
        // Close software keyboard pop up
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.confirmation_clear_search_history))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirmation_clear_search_history_yes)) { _, _ ->
                // Clear user recent search history
                SearchRecentSuggestions(
                    this,
                    SearchSuggestionProvider.AUTHORITY,
                    SearchSuggestionProvider.MODE
                )
                    .clearHistory()
                imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            }
            .setNegativeButton(getString(R.string.confirmation_clear_search_history_no)) { dialog, _ ->
                dialog.dismiss()
                imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            }

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(true)
        alert.setOnCancelListener { dialog ->
            dialog.dismiss()
            imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_view, menu)

        // Expand and request focus on search view
        val searchView = menu.findItem(R.id.searchPhotoActivity)
        searchView.expandActionView()
        searchView.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                finish()
                // return false to keep the search view always expand
                return false
            }
        })

        val componentName = ComponentName(this, SearchResultsActivity::class.java)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (searchView.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
        }
        return true
    }
}
