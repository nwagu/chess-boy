package com.nwagu.chessboy.sharedmodels.presentation

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nwagu.chessboy.sharedmodels.ChessApplication
import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel: AndroidViewModel {

    actual constructor(): this(ChessApplication.context!!)
    constructor(application: Application): super(application)

    actual val clientScope: CoroutineScope = viewModelScope
    actual override fun onCleared() {
        super.onCleared()
    }

    actual fun showToast(message: String) {
//        ContextCompat.getMainExecutor(getApplication()).execute {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
//        }
    }
}