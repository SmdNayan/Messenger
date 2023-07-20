package com.alex.messenger.utlis

import android.content.Context
import android.text.TextUtils
import com.alex.messenger.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordValid(): Boolean {
    if (this.length < 6) return false
    if (this.firstOrNull { it.isDigit() } == null) return false
    if (this.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
    if (this.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
    return true
}

fun setImages(context: Context, url: String, imageView: CircleImageView){
    Glide.with(context).load(url).placeholder(R.drawable.app_logo).into(imageView)
}

fun getDates(): String{
    val c: Calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return sdf.format(c.time)
}

fun getTime(date: String?): String? {
    val dateFormat = SimpleDateFormat()
    dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss")
    var dat: Date? = null
    val current = Date()
    try {
        dat = dateFormat.parse(UTL(date))
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    if (dat != null) {
        val seconds = (current.time - dat.time) / 1000
        var elapsed: String? = ""
        if (seconds <= 0) {
            elapsed = "1s"
        }
        elapsed = if (seconds > 0 && seconds < 60) {
            seconds.toString() + "s"
        } else if (seconds < 60 * 60) {
            val minutes = seconds / 60
            val minText = "m"
            "$minutes $minText"
        } else if (seconds < 24 * 60 * 60) {
            val hours = seconds / (60 * 60)
            val hourText = "hr"
            "$hours $hourText"
        } else if (seconds < 24 * 60 * 60 * 2) {
            "1d"
        } else if (seconds < 24 * 60 * 60 * 3) {
            "2d"
        } else if (seconds < 24 * 60 * 60 * 4) {
            "3d"
        } else if (seconds < 24 * 60 * 60 * 5) {
            "4d"
        } else if (seconds < 24 * 60 * 60 * 6) {
            "5d"
        } else if (seconds < 24 * 60 * 60 * 7) {
            "6d"
        } else if (seconds < 24 * 60 * 60 * 8) {
            "7d"
        } else {
            dateFormat.applyPattern("dd MMM yy")
            dateFormat.format(dat)
        }
        return elapsed
    }
    dateFormat.applyPattern("dd MMM yy")
    return dateFormat.format(dat)
}

fun UTL(date: String?): String? {
    val dateFormat = SimpleDateFormat()
    dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss")
    dateFormat.timeZone =TimeZone.getDefault()
    var dt: Date? = Date()
    val comingDate = date ?: dateFormat.format(dt)
    return try {
        dt = dateFormat.parse(comingDate)
        dateFormat.timeZone = TimeZone.getDefault()
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss")
        dateFormat.format(dt)
    } catch (e: ParseException) {
        date
    }
}
