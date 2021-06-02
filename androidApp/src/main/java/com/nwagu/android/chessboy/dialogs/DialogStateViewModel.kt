package com.nwagu.android.chessboy.dialogs

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DialogStateViewModel: ViewModel() {

    private val _dialogStates = hashMapOf<String, Boolean>()

    val dialogStates = MutableStateFlow<HashMap<String, Boolean>>(hashMapOf())

    fun setDialogStateValue(key: String, state: Boolean) {
        _dialogStates[key] = state
        dialogStates.value = _dialogStates.filter { it.value } as HashMap
    }
}