package com.example.epicture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_favorite.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class favoritePage : Fragment(R.layout.page_favorite) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retrofit.create(FavoritesService::class.java)
        val pictureRequest = service.listFavorites("Bearer " + client.accessToken.toString(), client.accountUsername.toString())
        pictureRequest.enqueue(object : Callback<Pictures> {
            override fun onResponse(call: Call<Pictures>, response: Response<Pictures>) {
                try {
                    if (response.isSuccessful) {
                        val allPictures = ArrayList<PictureItem>()
                        val response = response.body()
                        response!!.data.forEach { it ->
                            var pic = PictureItem(it)
                            pic.data.link = "https://i.imgur.com/" + pic.data.cover + pic.data.type.replace("image/", ".").replace("video/", ".").replace("gif/", ".")
                            allPictures.add(pic)
                        }
                        val postListAdapter: PostListAdapter = PostListAdapter(allPictures)
                        favorite_list.layoutManager = LinearLayoutManager(activity)
                        favorite_list.adapter = postListAdapter
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<Pictures>, t: Throwable) {
                error("KO")
            }
        })
    }
}