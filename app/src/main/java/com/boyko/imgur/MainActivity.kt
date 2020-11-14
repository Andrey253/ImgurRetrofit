package com.boyko.imgur

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.boyko.imgur.helpers.DocumentHelper
import com.boyko.imgur.helpers.IntentHelper
import com.boyko.imgur.services.UploadService
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.io.File

class MainActivity : AppCompatActivity() {

    private var uploadImage: ImageView? = null
    private var uploadTitle: EditText? = null
    private var uploadDesc: EditText? = null
    private var button: Button? = null
    private var upload: Upload? = null // Upload object containging image and meta data
    private var chosenFile: File? = null //chosen file from intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buildViews()
    }
    fun buildViews(){
        //val bundle = this.intent.extras

        uploadImage = findViewById(R.id.imageView)
        uploadTitle = findViewById(R.id.editText_upload_title)
        uploadDesc = findViewById(R.id.editText_upload_desc)
        button = findViewById(R.id.button)

        button!!.setOnClickListener(onUploadImage)
        uploadImage!!.setOnClickListener(onChoseImage)
    }
    private val onChoseImage = View.OnClickListener{
        uploadDesc!!.clearFocus()
        uploadTitle!!.clearFocus()
        IntentHelper.chooseFileIntent(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != IntentHelper.FILE_PICK) {
            return
        }

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        val returnUri: Uri? = data!!.data

        // Сheck permission.READ_EXTERNAL_STORAGE

        val filePath = DocumentHelper.getPath(this, returnUri!!)
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return
        chosenFile = File(filePath)

        /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */
        Picasso
            .get()
            .load(chosenFile!!)
            .placeholder(R.drawable.ic_baseline_image_24)
            .fit()
            .into(uploadImage)

    }
    private val onUploadImage = View.OnClickListener {
        /*
        Create the @Upload object
        */
        if (chosenFile != null) {
            createUpload(chosenFile!!)

            /*
            Start upload
            */
            UploadService(this).Execute(upload!!, UiCallback())
        }
    }
    private fun createUpload(image: File) {
        upload = Upload()

        upload!!.image = image
        upload!!.title = uploadTitle!!.text.toString()
        upload!!.description = uploadDesc!!.text.toString()
        //upload!!.albumId = ""
    }
    private inner class UiCallback : Callback<ImageResponse> {

        override fun success(imageResponse: ImageResponse, response: Response) {
            clearInput()
            val toast = Toast.makeText(applicationContext, imageResponse.data!!.link, Toast.LENGTH_LONG)
            println("mytag imgur imageResponse.data!!.link = ${imageResponse.data!!.link}")
            toast.show()
        }

        override fun failure(error: RetrofitError?) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById<View>(R.id.rootView), "No internet connection", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun clearInput() {
        uploadTitle!!.setText("")
        uploadDesc!!.clearFocus()
        uploadDesc!!.setText("")
        uploadTitle!!.clearFocus()
        uploadImage!!.setImageResource(R.drawable.ic_baseline_image_24)
    }
}