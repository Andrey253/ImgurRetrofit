package com.boyko.imgur.utils

import android.util.Log
import com.boyko.imgur.Constants

/*
 * Basic logger bound to a flag in Constants.java
 */
object aLog {
    fun w(TAG: String?, msg: String?) {
        if (Constants.LOGGING) {
            if (TAG != null && msg != null)
                Log.w(TAG, msg)
        }
    }

}
