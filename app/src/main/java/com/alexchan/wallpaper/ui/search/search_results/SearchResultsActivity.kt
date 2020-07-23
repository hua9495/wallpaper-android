package com.alexchan.wallpaper.ui.search.search_results

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.database.SearchSuggestionProvider
import com.alexchan.wallpaper.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class SearchResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Search Wallpaper Navigation Host
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostFragment)
        val navController = navHostFragment?.findNavController()
        navController?.setGraph(R.navigation.search_wallpaper)

        // Change topToolbar navigation icon
        /*topToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios)
        topToolbar.setNavigationOnClickListener {navigateBackToMainActivity()}
        topToolbar.title = intent.getStringExtra(SearchManager.QUERY)*/

        // Hide Bottom Navigation
        bottomNavigation.visibility = View.GONE

        handleIntent(intent)
    }

    private fun navigateBackToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            // Save query to recent query
            query?.also { query ->
                SearchRecentSuggestions(
                    this,
                    SearchSuggestionProvider.AUTHORITY,
                    SearchSuggestionProvider.MODE
                )
                    .saveRecentQuery(query, null)
            }

            // Use the query to search data
            if (!query.isNullOrEmpty()) {
                Toast.makeText(this, "Searching: $query", Toast.LENGTH_LONG).show()
            }
        }
    }
}
