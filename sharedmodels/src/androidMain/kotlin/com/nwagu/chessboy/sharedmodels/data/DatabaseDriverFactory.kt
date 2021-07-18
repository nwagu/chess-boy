package com.nwagu.chessboy.sharedmodels.data

import android.content.Context
import com.nwagu.chessboy.ChessBoyDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ChessBoyDatabase.Schema, context, "chessboy.db")
    }
}