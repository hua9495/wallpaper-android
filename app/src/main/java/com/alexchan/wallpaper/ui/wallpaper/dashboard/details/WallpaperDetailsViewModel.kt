package com.alexchan.wallpaper.ui.wallpaper.dashboard.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.model.unsplash.Photo
import com.alexchan.wallpaper.service.web.UnsplashApi
import com.alexchan.wallpaper.ui.wallpaper.dashboard.dashboard.UnsplashApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class WallpaperDetailsViewModel(userPhoto: Photo, app: Application) : AndroidViewModel(app) {

    // User's Photo Status
    private val _status = MutableLiveData<UnsplashApiStatus>()

    val status: LiveData<UnsplashApiStatus>
        get() = _status

    // MutableLiveData List <Photo>
    private val _userPhotos = MutableLiveData<List<Photo>>()

    val userPhotos: LiveData<List<Photo>>
        get() = _userPhotos

    private val _userPhotoCollection = MutableLiveData<Photo>()

    val userPhotoCollection: LiveData<Photo>
        get() = _userPhotoCollection

    val displayUserLocation = Transformations.map(userPhotoCollection) {
        Log.d("Display Location", it.user!!.location.toString())
        app.applicationContext.getString(R.string.userLocation,
            when (it.user.isLocationNullorUnknown) {
                true -> app.applicationContext.getString(R.string.userLocationUnknown)
                false -> it.user!!.location
        })
    }

    val displayUserImage = Transformations.map(userPhotoCollection) {
        Log.d("Display Image", it.user?.profileImage?.large)
        it.user?.profileImage?.large
    }

    val displayUserTotalPhotos = Transformations.map(userPhotoCollection) {
        Log.d("Display Photo", it.user?.totalPhotos.toString())
        app.applicationContext.getString(
            when (it.user!!.isTotalPhotoEqualOne) {
                true -> R.string.userTotalPhoto
                false -> when (it.user!!.isTotalPhotosOverThousand) {
                    true -> R.string.userTotalThousandOverPhotos
                    false -> R.string.userTotalPhotos
                }
            }, when (it.user.isTotalPhotosOverThousand) {
                true -> {
                    convertThousandRoundingToOneDecimal((it.user!!.totalPhotos).toFloat())
                }
                false -> it.user!!.totalPhotos
            })
    }

    val displayUserTotalLiked = Transformations.map(userPhotoCollection) {
        Log.d("Display Liked", it.user!!.totalLikes.toString())
        app.applicationContext.getString(
            when (it.user.isTotalLikesOverThousand) {
                true -> R.string.userTotalThousandOverLiked
                false -> R.string.userTotalLiked
        }, when (it.user.isTotalLikesOverThousand) {
                true -> {
                    convertThousandRoundingToOneDecimal((it.user!!.totalLikes).toFloat())
                }
                false -> it.user!!.totalLikes
            })
    }

    val displayUserTotalCollections = Transformations.map(userPhotoCollection) {
        Log.d("Display Collections", it.user!!.totalCollections.toString())
        app.applicationContext.getString(when (it.user!!.isTotalCollectionEqualOne) {
            true -> R.string.userTotalCollection
            false -> R.string.userTotalCollections
        }, it.user!!.totalCollections)
    }

    // Formula to Convert Likes and Photos which is over a Thousand to 1 decimal place
    private fun convertThousandRoundingToOneDecimal(userTotalValue: Float): Float {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.FLOOR
        Log.d("Display Calculation", df.format((userTotalValue / 1000)))
        return (df.format((userTotalValue / 1000))).toFloat()
    }

    init {
        _userPhotoCollection.value = userPhoto
        Log.d("init", userPhoto.user!!.username!!)
        loadPhotoCollection(userPhoto.user!!.username!!)
    }

    private fun loadPhotoCollection(username: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            // Get List<Photos>
            var getUserPhotosDeferred = UnsplashApi.retrofitService.getUserPhotosAsync(username!!)

            try {
                // For Collection
                _status.value = UnsplashApiStatus.LOADING
                var userPhotoLists = getUserPhotosDeferred.await()
                _status.value = UnsplashApiStatus.DONE

                userPhotoLists.forEach {
                    Log.d("List", it.id.toString())
                    Log.d("List", it.user.toString())
                    Log.d("List", it.photoUrl.raw)
                }

                // Set up Photo Collection Recycler View
                if (userPhotoLists.size > 0) {
                    _userPhotos.value = userPhotoLists

                    // For testing purposes
                    userPhotoLists.forEach{
                        Log.d("List Photos", it.photoUrl.raw)
                    }
                }

            } catch (e: Exception) {
                _status.value = UnsplashApiStatus.ERROR
                _userPhotos.value = ArrayList()
            }
        }
    }
}
