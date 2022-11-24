package com.example.epicture

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import kotlinx.android.synthetic.main.page_main.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class mainPage : Fragment(R.layout.page_main) {
    var lastFilter: String = "Popular"
    var section: String = "hot"
    var sort: String = "viral"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getGall(section, sort)
        button_filter.setOnClickListener{
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val dialog = MaterialDialog(activity!!).noAutoDismiss().customView(R.layout.dialog_custom_fragment)
        when (lastFilter) {
            "Popular" -> {
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).check(R.id.viral_popular)
            }
            "Best" -> {
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).check(R.id.viral_best)
            }
            "Newest" -> {
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).check(R.id.viral_newest)
            }
            "Famous" -> {
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).check(R.id.user_famous)
            }
            "Rising" -> {
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).check(R.id.user_rising)
            }
            "Latest" -> {
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).check(R.id.user_latest)
            }
        }
        dialog.findViewById<Button>(R.id.done_filter).setOnClickListener{
            val selectedFilter = dialog.getCustomView().findViewById<RadioButton>(
                dialog.findViewById<RadioGroup>(R.id.FirstRadioGroup).checkedRadioButtonId
            )
            lastFilter = selectedFilter.text.toString()
            when (lastFilter) {
                "Popular" -> {
                    section = "hot"
                    sort = "viral"
                }
                "Best" -> {
                    section = "hot"
                    sort = "top"
                }
                "Newest" -> {
                    section = "hot"
                    sort = "time"
                }
                "Famous" -> {
                    section = "user"
                    sort = "top"
                }
                "Rising" -> {
                    section = "user"
                    sort = "rising"
                }
                "Latest" -> {
                    section = "user"
                    sort = "time"
                }
            }
            dialog.dismiss()
            getGall(section, sort)
        }
        dialog.show()
    }

    private fun getGall(sec: String, sor: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retrofit.create(FilterService::class.java)
        val pictureRequest = service.listFilter("Bearer " + client.accessToken.toString(), sec, sor)
        pictureRequest.enqueue(object : Callback<Pictures> {
            override fun onResponse(call: Call<Pictures>, response: Response<Pictures>) {
                try {
                    if (response.isSuccessful) {
                        val allPictures = ArrayList<PictureItem>()
                        val response = response.body()
                        response!!.data.forEach { it ->
                            val pic = PictureItem(it)
                            if (pic.data.cover != null)
                                pic.data.link = "https://i.imgur.com/" + pic.data.cover + ".png"
                            allPictures.add(pic)
                        }
                        val postListAdapter: PostListAdapter = PostListAdapter(allPictures)
                        main_list.layoutManager = LinearLayoutManager(activity)
                        main_list.adapter = postListAdapter
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