package com.boyko.imgur.utils

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException

/*
 * Basic network utils
 */
object NetworkUtils {
    val TAG = NetworkUtils::class.java.simpleName

    fun isConnected(mContext: Context): Boolean {
        try {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (ex: Exception) {
            aLog.w(TAG, ex.message)
        }

        return false
    }
}
