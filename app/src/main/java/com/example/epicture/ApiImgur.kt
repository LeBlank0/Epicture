package com.example.epicture

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

data class Post(
    val data: String,
)

data class Picture(
    val title: String,
    val description: String,
    var link: String,
    val type: String,
    val id: String,
    val cover: String,
    val favorite: Boolean
)

data class Pictures(val data: List<Picture>)
data class PictureItem(val data: Picture)

interface PicturesService {
    @GET("/3/account/{username}/images")
    fun listPictures(
        @Header("authorization") authHeader: String,
        @Path("username") username: String
    ): Call<Pictures>
}

interface FavoritesService {
    @GET("/3/account/{username}/favorites")
    fun listFavorites(
        @Header("authorization") authHeader: String,
        @Path("username") username: String
    ): Call<Pictures>
}

interface FavPicService {
    @POST("/3/image/{hash}/favorite")
    fun favPic(
        @Header("authorization") authHeader: String,
        @Path("hash") hash: String
    ): Call<Post>
}

interface SearchService {
    @GET("/3/gallery/search/top/all")
    fun listSearch(
        @Header("authorization") authHeader: String,
        @Query("q") tag: String
    ): Call<Pictures>
}

interface FilterService {
    @GET("/3/gallery/{section}/{sort}/week")
    fun listFilter(
        @Header("authorization") authHeader: String,
        @Path("section") section: String,
        @Path("sort") sort: String,
    ): Call<Pictures>
}

interface UploadService {
    @Multipart
    @POST("/3/image")
    fun uploadImage(
        @Header("Authorization") authHeader: String,
        @Part image: MultipartBody.Part,
        @PartMap queries: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<Post>
}