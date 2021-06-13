package com.nwagu.android.chessboy.dialogs

import android.os.Bundle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DialogStateViewModel: ViewModel() {

    companion object {
        const val KEY_DIALOG_VISIBLE = "dialog_visible"
    }

    private val _dialogStates = hashMapOf<String, Bundle>()

    val dialogStates = MutableStateFlow<HashMap<String, Bundle>>(hashMapOf())

    fun setDialogStateValue(key: String, data: Bundle) {
        _dialogStates[key] = data
        dialogStates.value = _dialogStates.filter { it.value.getBoolean(KEY_DIALOG_VISIBLE) } as HashMap
    }
}