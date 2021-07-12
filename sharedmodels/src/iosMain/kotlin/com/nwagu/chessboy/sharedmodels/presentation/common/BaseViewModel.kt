package com.nwagu.chessboy.sharedmodels.presentation.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

actual open class BaseViewModel() {
    private val viewModelJob = SupervisorJob()
    val viewModelScope: CoroutineScope = CoroutineScope(ioDispatcher + viewModelJob)

    actual val clientScope: CoroutineScope = viewModelScope

    protected actual open fun onCleared() {
        viewModelJob.cancelChildren()
    }
}