package com.example.epicture

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.VideoView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_with_image.view.*
import kotlinx.android.synthetic.main.item_with_video.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val POST_TYPE_VIDEO: Int = 0
private const val POST_TYPE_IMAGE: Int = 1

class PostListAdapter(var postListItems: ArrayList<PictureItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(pictureItem: PictureItem) {
            itemView.image_post_title.text = pictureItem.data.title
            itemView.image_post_description.text = pictureItem.data.description
            Glide.with(itemView.context).load(pictureItem.data.link).into(itemView.image_post_image)
            if (pictureItem.data.favorite) {
                itemView.findViewById<ImageButton>(R.id.image_fav)
                    .setColorFilter(
                        Color.argb(255, 255, 255, 0)
                    )
            } else {
                itemView.findViewById<ImageButton>(R.id.image_fav)
                    .setColorFilter(
                        Color.argb(255, 0, 0, 0)
                    )
            }
            itemView.findViewById<ImageButton>(R.id.image_fav).setOnClickListener() {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.imgur.com")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                val service = retrofit.create(FavPicService::class.java)
                var id = pictureItem.data.id
                if (pictureItem.data.cover != null)
                    id = pictureItem.data.cover
                val pictureRequest = service.favPic("Bearer " + client.accessToken.toString(), id)
                pictureRequest.enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        try {
                            if (response.isSuccessful) {
                                val response = response.body()
                                if (response!!.data == "favorited") {
                                    itemView.findViewById<ImageButton>(R.id.image_fav)
                                        .setColorFilter(
                                            Color.argb(255, 255, 255, 0)
                                        )
                                } else {
                                    itemView.findViewById<ImageButton>(R.id.image_fav)
                                        .setColorFilter(
                                            Color.argb(255, 0, 0, 0)
                                        )
                                }
                            }
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        error("KO")
                    }
                })
            }
        }
    }

    class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(pictureItem: PictureItem) {
            itemView.video_post_title.text = pictureItem.data.title
            itemView.video_post_description.text = pictureItem.data.description
            itemView.video_post_video.setVideoPath(pictureItem.data.link)
            itemView.video_post_video.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == POST_TYPE_IMAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_with_image, parent, false)
            return ImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_with_video, parent, false)
            return VideoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == POST_TYPE_IMAGE) {
            (holder as ImageViewHolder).bind(postListItems[position])
        } else {
            (holder as VideoViewHolder).bind(postListItems[position])
        }
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun getItemViewType(position: Int): Int {
//        return if (postListItems[position].data.type.contains("video")) {
//            POST_TYPE_VIDEO
//        } else {
//            POST_TYPE_IMAGE
//        }
        return POST_TYPE_IMAGE
    }

}