package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.chessboy.sharedmodels.data.DatabaseDriverFactory
import com.nwagu.chessboy.sharedmodels.data.LocalGamesHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

actual open class BaseViewModel {
    private val viewModelJob = SupervisorJob()
    val viewModelScope: CoroutineScope = CoroutineScope(ioDispatcher + viewModelJob)

    actual val clientScope: CoroutineScope = viewModelScope
    actual val gamesHistoryRepository by lazy {
        LocalGamesHistoryRepository(DatabaseDriverFactory())
    }

    protected actual open fun onCleared() {
        viewModelJob.cancelChildren()
    }

    actual fun showToast(message: String) {
        //
    }
}