package com.nwagu.chessboy.util

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.nwagu.chessboy.screens.main.MainActivity

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
object Utils {

    fun unwrap(context: Context): MainActivity {
        var _context: Context = context
        while (_context !is MainActivity && _context is ContextWrapper) {
            _context = _context.baseContext
        }
        return _context as MainActivity
    }
}