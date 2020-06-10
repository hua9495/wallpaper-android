package com.alexchan.wallpaper.service.web

import com.alexchan.wallpaper.model.unsplash.Collection
import com.alexchan.wallpaper.model.unsplash.Photo
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val ACCESS_KEY = "3O-0lx3tK8EB-5FUQnneiHneMwC0waml_N4jrNvv2pQ"

private const val BASE_URL = "https://api.unsplash.com/"

// For example: https://api.unsplash.com/search/photos?query=london&client_id=3O-0lx3tK8EB-5FUQnneiHneMwC0waml_N4jrNvv2pQ

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Client-ID $ACCESS_KEY")
            .build()
        return chain.proceed(request)
    }
}).build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface UnsplashApiService {
    @GET("photos")
    fun getPhotos():
            Deferred<List<Photo>>

    @GET("users/{username}/collections")
    fun getCollections(@Path("username") type: String):
            Deferred<ArrayList<Collection>>

    @GET("collections/{id}")
    fun getCollectionsPhoto(@Path("id") type: Int):
            Deferred<Collection>
}

// Singleton too expensive to call - so call once
object UnsplashApi {
    val retrofitService : UnsplashApiService by lazy {
        retrofit.create(UnsplashApiService::class.java)
    }
}
