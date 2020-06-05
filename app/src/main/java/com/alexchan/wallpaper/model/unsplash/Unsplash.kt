package com.alexchan.wallpaper.model.unsplash

import com.squareup.moshi.Json

data class Photo(
    @Json(name = "id")
    val id: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "urls")
    val photoUrl: PhotoUrl,
    @Json(name = "user")
    val user: User?
)

data class PhotoUrl(
    @Json(name = "raw")
    val raw: String,
    @Json(name = "full")
    val full: String,
    @Json(name = "regular")
    val regular: String
)

data class User(
    @Json(name = "id")
    val id: String?,
    @Json(name = "username")
    val username: String?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "total_likes")
    val totalLikes: Int,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "profile_image")
    val profileImage: ProfileImage?
)

data class ProfileImage(
    @Json(name = "small")
    val small: String?,
    @Json(name = "medium")
    val medium: String?,
    @Json(name = "large")
    val large: String?
)

data class Collection(
    @Json(name = "id")
    val id: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "cover_photo")
    val coverPhoto: Photo?,
    @Json(name = "user")
    val user: User?
)