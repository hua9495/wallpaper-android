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

enum class UnsplashApiStatus { LOADING, ERROR, DONE }

class MainNewViewModel : ViewModel() {
    
    private val _status = MutableLiveData<UnsplashApiStatus>()

    private val _photoProperties = MutableLiveData<List<Photo>>()
    
    val status: LiveData<UnsplashApiStatus>
        get() = _status

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
                _status.value = UnsplashApiStatus.LOADING
                var listPhotos = getPhotosDeferred.await()
                _status.value = UnsplashApiStatus.DONE
                if (listPhotos.size > 0) {
                    _photoProperties.value = listPhotos

                    // For testing purposes
                    /*listPhotos.forEach{
                        Log.d("Photos", it.photoUrl.raw)
                    }*/
                }
            } catch (e: Exception) {
                _status.value = UnsplashApiStatus.ERROR
                _photoProperties.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // To stop loading data when the ViewModel is destroyed
        viewModelJob.cancel()
    }
}