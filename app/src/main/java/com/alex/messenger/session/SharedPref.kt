package com.alex.messenger.session

import android.content.Context
import android.content.SharedPreferences

class SharedPref(val context: Context) {

    private var preference: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val sharedPrefKey = "hishab_user"

    init {
        preference = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        editor = preference!!.edit()
    }

    fun setSignIn(isLogIn: Boolean) {
        editor!!.putBoolean("IS_SIGNIN", isLogIn)
        editor!!.apply()
    }

    fun getSignIn(): Boolean {
        return preference!!.getBoolean("IS_SIGNIN", false)
    }

    fun setUserId(userId: String) {
        editor!!.putString("UID", userId)
        editor!!.apply()
    }

    fun getUserId(): String? {
        return preference!!.getString("UID", "")
    }

    fun setUserName(userId: String) {
        editor!!.putString("U_NAME", userId)
        editor!!.apply()
    }

    fun getUserName(): String? {
        return preference!!.getString("U_NAME", "")
    }

    fun setUserProfession(userId: String) {
        editor!!.putString("PROFESSION", userId)
        editor!!.apply()
    }

    fun getUserProfession(): String? {
        return preference!!.getString("PROFESSION", "")
    }

    fun setImageUrl(str: String) {
        editor!!.putString("P_I_URL", str)
        editor!!.apply()
    }

    fun getImageUrl(): String? {
        return preference!!.getString("P_I_URL", "")
    }
}