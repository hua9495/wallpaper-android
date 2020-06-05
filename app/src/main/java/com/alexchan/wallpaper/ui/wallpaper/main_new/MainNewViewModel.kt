package com.alexchan.wallpaper.ui.wallpaper.main_new

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexchan.wallpaper.model.unsplash.Photo
import com.alexchan.wallpaper.service.web.UnsplashApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainNewViewModel : ViewModel() {
    
    private val _mainNewText = MutableLiveData<String>()

    private val _photoProperties = MutableLiveData<List<Photo>>()
    
    val mainNewText: LiveData<String>
        get() = _mainNewText

    val photoProperties: LiveData<List<Photo>>
        get() = _photoProperties

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

                if (listPhotos.size > 0) {
                    _photoProperties.value = listPhotos

                    // For testing purposes
                    /*listPhotos.forEach{
                        Log.d("Photos", it.photoUrl.raw)
                    }*/
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