package com.nwagu.android.chessboy.dialogs

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberDialogController(viewModelStoreOwner: ViewModelStoreOwner): DialogController {
    return DialogController(viewModelStoreOwner)
}

class DialogController(viewModelStoreOwner: ViewModelStoreOwner) {

    private val viewModel = ViewModelProvider(viewModelStoreOwner).get(DialogStateViewModel::class.java)

    fun getStates(): MutableStateFlow<HashMap<String, Boolean>> {
        return viewModel.dialogStates
    }

    fun showDialog(key: String) {
        viewModel.setDialogStateValue(key, true)
    }

    fun quitDialog(key: String) {
        viewModel.setDialogStateValue(key, false)
    }

}