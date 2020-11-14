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

    fun connectionReachable(): Boolean {
        var socket: Socket? = null
        var reachable = false
        try {
            socket = Socket("google.com", 80)
            reachable = socket.isConnected
        } catch (e: UnknownHostException) {
            aLog.w(TAG, "Error connecting to server")
            reachable = false
        } catch (e: IOException) {
            aLog.w(TAG, "Error connecting to server")
        } finally {
            if (socket != null) {
                try {
                    socket.close()
                } catch (e: IOException) {
                    aLog.w(TAG, "Error closing connecting socket test")
                }

            }
        }
        aLog.w(TAG, "Data connectivity change detected, ping test=" + reachable.toString())
        return reachable
    }

}
