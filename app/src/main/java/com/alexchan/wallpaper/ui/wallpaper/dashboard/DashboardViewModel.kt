package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexchan.wallpaper.model.unsplash.Photo
import com.alexchan.wallpaper.service.web.UnsplashApi
import com.alexchan.wallpaper.ui.MainActivity.Companion.searchQuery
import com.alexchan.wallpaper.ui.MainActivity.Companion.searchStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class UnsplashApiStatus { LOADING, ERROR, DONE }

class DashboardViewModel : ViewModel() {
    
    private val _status = MutableLiveData<UnsplashApiStatus>()

    // List <Photo>
    private val _photoProperties = MutableLiveData<List<Photo>>()
    
    val status: LiveData<UnsplashApiStatus>
        get() = _status

    // List <Photo>
    val photoProperties: LiveData<List<Photo>>
        get() = _photoProperties

    private val _navigateToUserCollection = MutableLiveData<Photo>()

    val navigateToUserCollection: LiveData<Photo>
        get() = _navigateToUserCollection

    init {
        if (!searchStatus) {
            getUnsplashPhotos()
        } else {
            getUnsplashSearchPhotos(searchQuery)
        }
    }
    
    private fun getUnsplashPhotos() {
        viewModelScope.launch(Dispatchers.Main) {
            // Get List <Photo>
            var getListPhotosDeferred = UnsplashApi.retrofitService.getPhotosAsync()

            try {
                _status.value = UnsplashApiStatus.LOADING
                var listPhotos = getListPhotosDeferred.await()
                _status.value = UnsplashApiStatus.DONE

                if (listPhotos.size > 0) {
                    _photoProperties.value = listPhotos

                    // For testing purposes
                    listPhotos.forEach{
                        Log.d("Photos", it.photoUrl.raw)
                    }
                }

            } catch (e: Exception) {
                _status.value = UnsplashApiStatus.ERROR
                _photoProperties.value = ArrayList()
            }
        }
    }

    fun getUnsplashSearchPhotos(searchQuery: String) {
        viewModelScope.launch(Dispatchers.Main) {
            // Get List <Photo>
            var getListSearchedPhotosDeferred = UnsplashApi.retrofitService.getSearchPhotosAsync(searchQuery)

            try {
                _status.value = UnsplashApiStatus.LOADING
                var listSearchedPhotos = getListSearchedPhotosDeferred.await()
                _status.value = UnsplashApiStatus.DONE

                Log.d("Photos", listSearchedPhotos.results.size.toString())

                if (listSearchedPhotos.results.size > 0) {
                    _photoProperties.value = listSearchedPhotos.results

                    // For testing purposes
                    listSearchedPhotos.results.forEach{
                        //Log.d("Photos", it.photoUrl.raw)
                        Log.d("Photos", it.photoUrl.raw)
                    }
                }

            } catch (e: Exception) {
                _status.value = UnsplashApiStatus.ERROR
                _photoProperties.value = ArrayList()
            }
        }
    }

    fun displayUserCollection(userPhoto: Photo) {
        _navigateToUserCollection.value = userPhoto
    }

    fun displayUserCollectionComplete() {
        _navigateToUserCollection.value = null
    }
}
