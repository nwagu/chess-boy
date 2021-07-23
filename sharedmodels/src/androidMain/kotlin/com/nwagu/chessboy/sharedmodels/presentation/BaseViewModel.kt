package com.nwagu.chessboy.sharedmodels.presentation

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nwagu.chessboy.sharedmodels.ChessApplication
import com.nwagu.chessboy.sharedmodels.data.DatabaseDriverFactory
import com.nwagu.chessboy.sharedmodels.data.LocalGamesHistoryRepository
import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel: AndroidViewModel {

    actual constructor(): this(ChessApplication.context!!)
    constructor(application: Application): super(application)

    actual val clientScope: CoroutineScope = viewModelScope
    actual val gamesHistoryRepository by lazy {
        LocalGamesHistoryRepository(DatabaseDriverFactory(getApplication()))
    }

    actual override fun onCleared() {
        super.onCleared()
    }

    actual fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show()
    }
}