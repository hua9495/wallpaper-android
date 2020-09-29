package com.alexchan.wallpaper.util.shared_preferences

import android.content.Context
import android.content.SharedPreferences

class DisplayViewTypePreferences(context: Context) {

    // Use Shared Preference to get user selected display view type
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(DISPLAY_VIEW_TYPE, Context.MODE_PRIVATE)

    // Get user selection for display view type
    fun getUserSelectionDisplayViewType(): Int {
        return sharedPreferences.getInt(DISPLAY_VIEW_TYPE_KEY, 1)
    }

    fun setUserSelectionDisplayViewType(displayViewType: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(DISPLAY_VIEW_TYPE_KEY, displayViewType)
        editor.apply()
    }

    companion object {
        var DISPLAY_VIEW_TYPE: String = "DISPLAY_VIEW_TYPE"
        var DISPLAY_VIEW_TYPE_KEY: String = "display_view_type_key"
    }
}
