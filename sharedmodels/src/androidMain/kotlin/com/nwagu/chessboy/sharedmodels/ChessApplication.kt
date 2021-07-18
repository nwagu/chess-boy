package com.nwagu.chessboy.sharedmodels

import android.app.Application

open class ChessApplication: Application() {

    companion object {
        var context: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }


}
