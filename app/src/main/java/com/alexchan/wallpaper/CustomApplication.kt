package com.alexchan.wallpaper

import android.app.Application
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Unsplash Photopicker Initialization
        UnsplashPhotoPicker.init(
            this, // application
            "3O-0lx3tK8EB-5FUQnneiHneMwC0waml_N4jrNvv2pQ",
            "dO460Olac7h3zdOGPZZeOlUlkP_JPeMMutlSNBm4bnw"
            /* optional page size */
        )
    }
}