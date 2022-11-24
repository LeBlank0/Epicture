package com.example.epicture

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class searchPage : Fragment(R.layout.page_search) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_input.queryHint = "Search"
        search_input.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                val tag = query
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.imgur.com")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                val service = retrofit.create(SearchService::class.java)
                val pictureRequest = service.listSearch("Bearer " + client.accessToken.toString(), tag.toString())
                pictureRequest.enqueue(object : Callback<Pictures> {
                    override fun onResponse(call: Call<Pictures>, response: Response<Pictures>) {
                        try {
                            if (response.isSuccessful) {
                                val allPictures = ArrayList<PictureItem>()
                                val response = response.body()
                                response!!.data.forEach { it ->
                                    var pic = PictureItem(it)
                                    if (pic.data.cover != null)
                                        pic.data.link = "https://i.imgur.com/" + pic.data.cover + ".png"
                                    allPictures.add(pic)
                                }
                                val postListAdapter: PostListAdapter = PostListAdapter(allPictures)
                                search_list.layoutManager = LinearLayoutManager(activity)
                                search_list.adapter = postListAdapter
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
                return false
            }
        })
    }
}