package com.nwagu.android.chessboy.util

import android.content.Context

object SharedPrefUtils {

    fun readString(ctx: Context, settingName: String, defaultValue: String?): String? {
        val sharedPref = ctx.getSharedPreferences("pref_file", Context.MODE_PRIVATE)
        return sharedPref.getString(settingName, defaultValue)
    }

    fun saveString(ctx: Context, settingName: String, settingValue: String?) {
        val sharedPref = ctx.getSharedPreferences("pref_file", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(settingName, settingValue)
        editor.apply()
    }
}