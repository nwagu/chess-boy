package com.nwagu.chessboy.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun createViewModelFactory(viewModelConstructor: () -> ViewModel): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModelConstructor() as T
        }
    }
}