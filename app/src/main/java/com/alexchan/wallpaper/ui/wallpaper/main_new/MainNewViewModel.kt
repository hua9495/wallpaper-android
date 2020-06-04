package com.alexchan.wallpaper.ui.wallpaper.main_new

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexchan.wallpaper.service.web.UnsplashApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainNewViewModel : ViewModel() {
    
    private val _mainNewText = MutableLiveData<String>()
    
    val mainNewText: LiveData<String>
        get() = _mainNewText

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getUnsplashPhotos()
    }
    
    private fun getUnsplashPhotos() {
        coroutineScope.launch {
            var getPhotosDeferred = UnsplashApi.retrofitService.getPhotos()
            try {
                var listPhotos = getPhotosDeferred.await()

                _mainNewText.value = "Success: ${listPhotos.size} number of photos has been successfully retrieved!"

                listPhotos.forEach{
                    Log.d("Photos", "${it.id}")
                }
            } catch (e: Exception) {
                _mainNewText.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // To stop loading data when the ViewModel is destroyed
        viewModelJob.cancel()
    }
}