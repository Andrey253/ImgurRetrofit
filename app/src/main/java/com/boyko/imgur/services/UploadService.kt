package com.boyko.imgur.services

import android.content.Context
import java.lang.ref.WeakReference
import com.boyko.imgur.Constants
import com.boyko.imgur.helpers.NotificationHelper
import com.boyko.imgur.model.ImageResponse
import com.boyko.imgur.model.ImgurAPI
import com.boyko.imgur.model.Upload
import com.boyko.imgur.utils.NetworkUtils
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import retrofit.mime.TypedFile


/*
 * Our upload service. This creates our restadapter, uploads our image, and notifies us of the response.
 */
class UploadService(context: Context) {

    private val mContext: WeakReference<Context> = WeakReference(context)

    fun execute(upload: Upload, callback: Callback<ImageResponse>?) {
        val cb = callback

        if (!NetworkUtils.isConnected(mContext.get()!!)) {
            //Callback will be called, so we prevent a unnecessary notification
            cb!!.failure(null)
            return
        }

        val notificationHelper = NotificationHelper(mContext.get()!!)
        notificationHelper.createUploadingNotification()

        val restAdapter = buildRestAdapter()

        restAdapter.create(ImgurAPI::class.java).postImage(
                Constants.clientAuth,
                upload.title!!,
                upload.description!!,
                upload.albumId, "",
                TypedFile("image/*", upload.image!!),
                object : Callback<ImageResponse> {
                    override fun success(imageResponse: ImageResponse, response: Response?) {
                        cb?.success(imageResponse, response)
                        if (response == null) {
                            /*
                             Notify image was NOT uploaded successfully
                            */
                            notificationHelper.createFailedUploadNotification()
                            return
                        }
                        /*
                        Notify image was uploaded successfully
                        */
                        if (imageResponse.success) {
                            notificationHelper.createUploadedNotification(imageResponse)
                        }
                    }

                    override fun failure(error: RetrofitError) {
                        cb?.failure(error)
                        notificationHelper.createFailedUploadNotification()
                    }
                })
    }

    private fun buildRestAdapter(): RestAdapter {
        val imgurAdapter = RestAdapter.Builder()
                .setEndpoint(ImgurAPI.server)
                .build()

        /*
        Set rest adapter logging if we're already logging
        */
        if (Constants.LOGGING)
            imgurAdapter.logLevel = RestAdapter.LogLevel.FULL
        return imgurAdapter
    }
}
