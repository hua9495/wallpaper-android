package com.alexchan.wallpaper.model.unsplash

import com.squareup.moshi.Json

data class Photo(
    val id: String?,
    val description: String?,
    val likes: Int,
    @Json(name = "urls")
    val photoUrl: PhotoUrl,
    val user: User?
)

data class PhotoUrl(
    val raw: String,
    val full: String,
    val regular: String
)

data class User(
    val id: String?,
    val username: String?,
    val bio: String?,
    @Json(name = "total_likes")
    val totalLikes: Int,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "profile_image")
    val profileImage: ProfileImage?
)

data class ProfileImage(
    val small: String?,
    val medium: String?,
    val large: String?
)

data class Collection(
    val id: Int?,
    val title: String?,
    val description: String?,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "urls")
    val collectionPhotoUrl: PhotoUrl?,
    val user: User?
)
