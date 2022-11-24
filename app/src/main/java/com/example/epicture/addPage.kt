package com.example.epicture

import android.R.attr.bitmap
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_add.*
import kotlinx.android.synthetic.main.page_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File


class addPage : Fragment(R.layout.page_add) {
    private var MyPicBinary: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
        choose_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        upload_button.setOnClickListener {
            val file = File(getRealPathFromURI(MyPicBinary!!))

            val requestFile = RequestBody.create(MediaType.parse(activity!!.contentResolver.getType(MyPicBinary!!)), file)
            val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val titleBody = RequestBody.create(okhttp3.MultipartBody.FORM, text_title.text.toString())
            val descriptionBody = RequestBody.create(okhttp3.MultipartBody.FORM, text_description.text.toString())
            val optinalBodyMap = mapOf("title" to titleBody, "description" to descriptionBody)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.imgur.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(UploadService::class.java)
            val pictureRequest = service.uploadImage("Bearer " + client.accessToken.toString(), imageBody, optinalBodyMap)
            println(file.name)
            println(imageBody)
            pictureRequest.enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    try {
                        if (response.isSuccessful) {
                            val response = response.body()
                            println(response!!.data)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            image_view.setImageURI(data?.data)
            MyPicBinary = data?.data
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context!!, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val result = cursor?.getString(column_index!!)
        cursor?.close()
        return result
    }
}