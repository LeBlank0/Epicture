package com.example.epicture

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class profilePage : Fragment(R.layout.page_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retrofit.create(PicturesService::class.java)
        val pictureRequest = service.listPictures(
            "Bearer " + client.accessToken.toString(),
            client.accountUsername.toString()
        )
        pictureRequest.enqueue(object : Callback<Pictures> {
            override fun onResponse(call: Call<Pictures>, response: Response<Pictures>) {
                try {
                    if (response.isSuccessful) {
                        val allPictures = ArrayList<PictureItem>()
                        val response = response.body()
                        response!!.data.forEach { it ->
                            val pic = PictureItem(it)
                            allPictures.add(pic)
                        }
                        val postListAdapter: PostListAdapter = PostListAdapter(allPictures)
                        image_list.layoutManager = LinearLayoutManager(activity)
                        image_list.adapter = postListAdapter
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<Pictures>, t: Throwable) {
                error("KO")
            }
        })
        button_logout.setOnClickListener {
            client.logoutClient()
            requireActivity().run{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}