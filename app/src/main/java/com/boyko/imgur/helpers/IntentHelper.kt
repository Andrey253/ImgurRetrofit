package com.boyko.imgur.helpers

import android.app.Activity
import android.content.Intent

object IntentHelper {
    const val FILE_PICK = 1001


    fun chooseFileIntent(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        activity.startActivityForResult(intent, FILE_PICK)
    }
}
