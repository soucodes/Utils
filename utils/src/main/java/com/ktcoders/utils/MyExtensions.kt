package com.ktcoders.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.security.MessageDigest
import java.util.regex.Pattern

object MyExtensions {

    private var PATTERN_MOBILE_NUMBER: Pattern = Pattern.compile("^(\\+98|0098|98|0)?9\\d{9}$")

    private var PATTERN_EMAIL: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
    )


    /** short toast */
    fun Context.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /** long toast */
    fun Context.toast2(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    /** check connections such as wifi & mobile, not internet available */
    fun Context.isConnected(): Boolean {
        var isWifi = false
        var isMobile = false
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifi = isWifi or isConnected
                    Utils.log("wifi type")
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobile = isMobile or isConnected
                    Utils.log("data type")
                }
            }
        }
        return isMobile || isWifi
    }

    /** check any vpn is connected or not */
    fun Context.isVpnConnected(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN)
        return networkInfo?.isConnected ?: false
    }

    /** check Permission, input such as: Manifest.permission.CAMERA */
    fun Context.checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    /** check device use from auto time in setting or not */
    fun Context.isTimeZoneAutomatic(): Boolean {
        return Settings.Global.getInt(this.contentResolver, Settings.Global.AUTO_TIME_ZONE, 0) == 1
    }

    /** hide soft keyboard, input such as: EditText */
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /** start vibrate manual milliseconds */
    fun Context.startVibrate(milliSeconds: Long) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliSeconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(milliSeconds)
        }
    }

    /** get Signatures such as: SHA, MD5, SHA256 */
    @SuppressLint("PackageManagerGetSignatures")
    fun Context.getSignatures(key: String = "SHA", packageName: String): String {
//        packageName or BuildConfig.APPLICATION_ID
        try {
            val info = this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val messageDigest = MessageDigest.getInstance(key)
                messageDigest.update(signature.toByteArray())
                val digest = messageDigest.digest()
                val stringBuilder = StringBuilder()
                for (i in digest.indices) {
                    if (i != 0) stringBuilder.append(":")
                    val byte = digest[i].toInt() and 0xff
                    val hex = Integer.toHexString(byte)
                    if (hex.length == 1) stringBuilder.append("0")
                    stringBuilder.append(hex)
                }
                return stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "error"
    }

    /** use this before setContentLayout */
    fun Activity.keepScreenOn() {
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /** use this before setContentLayout */
    fun Activity.lockScreenShot(value: Boolean) {
        if (value) {
            this.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    /** check email pattern like test@test.com */
    fun String.isEmail(): Boolean = PATTERN_EMAIL.matcher(this).find()

    /** this is specific for iran and it can be start with: 0098,+98,09,9 */
    fun String.isMobileNumber(): Boolean = PATTERN_MOBILE_NUMBER.matcher(this).find()

    /** this depend on company policy */
    fun String.isUserName(): Boolean {
        return false // TODO:
    }

    /** this is specific for iran, it should be 10 digit and valid */
    fun String.isNationalCode(): Boolean {
        return false // TODO:
    }


}