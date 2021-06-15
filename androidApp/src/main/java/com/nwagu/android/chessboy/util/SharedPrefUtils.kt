package com.nwagu.android.chessboy.util

import android.content.Context
import com.nwagu.android.chessboy.constants.PreferenceKeys

object SharedPrefUtils {

    const val PGN_SEPARATOR = "\nxxxxxx\nxxxxxx\n"

    fun getSavedPGNs(context: Context): List<String> {
        return readString(context, PreferenceKeys.GAMES_HISTORY, "")?.split(PGN_SEPARATOR) ?: emptyList()
    }

    fun savePGNs(context: Context, pgns: List<String>) {
        return saveString(context, PreferenceKeys.GAMES_HISTORY, pgns.joinToString(PGN_SEPARATOR))
    }

    private fun readString(ctx: Context, prefName: String, defaultPrefValue: String?): String? {
        val sharedPref = ctx.getSharedPreferences("pref_file", Context.MODE_PRIVATE)
        return sharedPref.getString(prefName, defaultPrefValue)
    }

    private fun saveString(ctx: Context, prefName: String, prefValue: String?) {
        val sharedPref = ctx.getSharedPreferences("pref_file", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(prefName, prefValue)
        editor.apply()
    }

}