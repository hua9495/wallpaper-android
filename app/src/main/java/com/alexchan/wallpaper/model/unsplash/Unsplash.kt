package com.alexchan.wallpaper.model.unsplash

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String?,
    val description: String?,
    val likes: Int,
    @Json(name = "urls")
    val photoUrl: PhotoUrl,
    val user: User?
) : Parcelable

@Parcelize
data class PhotoUrl(
    val raw: String,
    val full: String,
    val regular: String
) : Parcelable

@Parcelize
data class User(
    val id: String?,
    val username: String?,
    val name: String?,
    val bio: String?,
    val location: String?,
    @Json(name = "total_collections")
    val totalCollections: Int,
    @Json(name = "total_likes")
    val totalLikes: Int,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "profile_image")
    val profileImage: ProfileImage?
) : Parcelable {
    val isLocationNull
        get() = location == null
    val isTotalPhotosOverThousand
        get() = totalPhotos > 1000
    val isTotalLikesOverThousand
        get() = totalLikes > 1000
}

@Parcelize
data class ProfileImage(
    val small: String?,
    val medium: String?,
    val large: String?
) : Parcelable

@Parcelize
data class Collection(
    val id: Int?,
    val title: String,
    val description: String?,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    val user: User?,
    @Json(name = "cover_photo")
    val coverPhoto: CoverPhoto,
    @Json(name = "preview_photos")
    val previewPhotos: List<PreviewPhotos>
) : Parcelable

@Parcelize
data class CoverPhoto(
    val id: String,
    @Json(name = "urls")
    val collectionCoverPhoto: PhotoUrl
) : Parcelable

@Parcelize
data class PreviewPhotos(
    val id: String,
    @Json(name = "urls")
    val collectionPreviewPhotos: PhotoUrl
) : Parcelable
