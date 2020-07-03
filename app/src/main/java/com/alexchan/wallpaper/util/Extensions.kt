package com.alexchan.wallpaper.util

// General purpose extensions

// For Log statement
val Any.TAG: String
    get() {
        return this.javaClass.simpleName
    }
