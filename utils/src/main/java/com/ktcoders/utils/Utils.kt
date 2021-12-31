package com.ktcoders.utils

import android.util.Log
import java.util.*

object Utils {

    fun log(msg: String) {
        Log.i("AAAAAAAAAA", msg)
    }

    /** it may not always true, google is blocked in some countries */
    fun isOnline(): Boolean {
        return try {
            Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    /** just check device language */
    fun isLanguageFarsi(): Boolean {
        return Locale.getDefault().language.equals("fa")
    }

    /** it may not always be true */
    fun isIranianUser(): Boolean {
        return if (Locale.getDefault().country.contains("ir", ignoreCase = true)) true
        else TimeZone.getDefault().id.contains("tehran", ignoreCase = true)
    }

    fun getIpAddress(): String {
        return "" // TODO:
    }


}