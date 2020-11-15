package com.boyko.imgur.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.lang.ref.WeakReference
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.boyko.imgur.R
import com.boyko.imgur.model.ImageResponse

/*
 *
 * This class is just created to help with notifications, definitely not necessary.
 */
class NotificationHelper(context: Context) {

    private val mContext: WeakReference<Context> = WeakReference(context)
    private val notificationManager: NotificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    fun createUploadingNotification() {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(mContext.get()!!, DEFAULT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setContentTitle(mContext.get()!!.getString(R.string.notification_progress))
            .setColor(mContext.get()!!.getResources().getColor(R.color.primary))
            .setAutoCancel(true)
            .build()

        val mNotificationManager = mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(mContext.get()!!.getString(R.string.app_name).hashCode(), notification)
    }

    fun createUploadedNotification(response: ImageResponse) {
        createNotificationChannel()

        notificationManager.cancel(REQUEST_CODE)

        val resultIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.data!!.link))
        val intent = PendingIntent.getActivity(mContext.get(), 0, resultIntent, 0)
        val mBuilder = NotificationCompat.Builder(mContext.get()!!, DEFAULT_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_menu_gallery)
        .setContentTitle(mContext.get()!!.getString(R.string.notifaction_success))
        .setContentText(response.data!!.link)
        .setContentIntent(intent)
        .setAutoCancel(true)
        mBuilder.color = ContextCompat.getColor(mContext.get()!!, R.color.primary)

        val shareIntent = Intent(Intent.ACTION_SEND, Uri.parse(response.data!!.link))
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, response.data!!.link)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val pIntent = PendingIntent.getActivity(mContext.get(), 0, shareIntent, 0)
        mBuilder.addAction(NotificationCompat.Action(R.drawable.abc_ic_menu_share_mtrl_alpha,
                mContext.get()!!.getString(R.string.notification_share_link), pIntent))

        val mNotificationManager = mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(mContext.get()!!.getString(R.string.app_name).hashCode(), mBuilder.build())
    }

    fun createFailedUploadNotification() {
        val mBuilder = NotificationCompat.Builder(mContext.get()!!, "channelId")
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert)
        mBuilder.setContentTitle(mContext.get()!!.getString(R.string.notification_fail))


        mBuilder.color = ContextCompat.getColor(mContext.get()!!, R.color.primary)

        mBuilder.setAutoCancel(true)

        val mNotificationManager = mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(mContext.get()!!.getString(R.string.app_name).hashCode(), mBuilder.build())
    }

    companion object {
        const val DEFAULT_CHANNEL_ID = "0"
        const val REQUEST_CODE = 111
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel"
            val descriptionText = "default channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DEFAULT_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                mContext.get()!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
