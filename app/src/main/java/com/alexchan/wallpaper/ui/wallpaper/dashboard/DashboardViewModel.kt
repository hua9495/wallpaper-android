package com.alexchan.wallpaper.ui.wallpaper.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexchan.wallpaper.model.unsplash.Collection
import com.alexchan.wallpaper.model.unsplash.Photo
import com.alexchan.wallpaper.service.web.UnsplashApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class UnsplashApiStatus { LOADING, ERROR, DONE }

class MainNewViewModel : ViewModel() {
    
    private val _status = MutableLiveData<UnsplashApiStatus>()

    // List <Photo>
    private val _photoProperties = MutableLiveData<List<Photo>>()

    // For Collection
    private val _photoCollectionProperty = MutableLiveData<Collection>()
    
    val status: LiveData<UnsplashApiStatus>
        get() = _status

    // List <Photo>
    val photoProperties: LiveData<List<Photo>>
        get() = _photoProperties

    // For Collection
    val photoCollectionProperty: LiveData<Collection>
        get() = _photoCollectionProperty

    init {
        getUnsplashPhotos()
    }
    
    private fun getUnsplashPhotos() {
        viewModelScope.launch(Dispatchers.Main) {
            // Get List <Photo>
            var getListPhotosDeferred = UnsplashApi.retrofitService.getPhotos()

            // Get <Collection>
            //var getCollectionPhotosDeferred = UnsplashApi.retrofitService.getCollectionsPhoto(9343982)

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

                /*
                ////// Show Photo Collection (still under development) \\\\\\

                var collectionPhotos = getCollectionPhotosDeferred.await()
                if (collectionPhotos != null) {
                    _photoCollectionProperty.value = collectionPhotos
                    Log.d("Collection", collectionPhotos.id.toString())
                    Log.d("Collection", collectionPhotos.collectionPhotoUrl!!.raw)
                }*/

            } catch (e: Exception) {
                _status.value = UnsplashApiStatus.ERROR
                _photoProperties.value = ArrayList()

                // For Collection
                //_photoCollectionProperty.value = null
            }
        }
    }
}
