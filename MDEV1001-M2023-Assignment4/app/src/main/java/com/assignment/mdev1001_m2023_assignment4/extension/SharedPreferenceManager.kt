package com.assignment.mdev1001_m2023_assignment4.extension

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceManager {
    val PREF_NAME = "Assignment4_pref"
    private var isInit = false
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    fun init(context: Context) {
        if (isInit)
            return
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = prefs!!.edit()
        isInit = true
    }


    fun putString(key: String, value: String) {
        editor!!.putString(key, value)
        editor!!.commit()
    }


    fun getString(key: String, defValue: String): String? {
        return prefs!!.getString(key, defValue)
    }


    fun removeAllData() {
        editor!!.clear().commit()

    }

    fun removeString(key: String) {
        editor!!.remove(key)
    }
}