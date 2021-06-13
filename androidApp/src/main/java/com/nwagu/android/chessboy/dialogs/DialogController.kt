package com.nwagu.android.chessboy.dialogs

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.nwagu.android.chessboy.dialogs.DialogStateViewModel.Companion.KEY_DIALOG_VISIBLE
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberDialogController(viewModelStoreOwner: ViewModelStoreOwner): DialogController {
    return DialogController(viewModelStoreOwner)
}

class DialogController(viewModelStoreOwner: ViewModelStoreOwner) {

    private val viewModel = ViewModelProvider(viewModelStoreOwner).get(DialogStateViewModel::class.java)

    fun getStates(): MutableStateFlow<HashMap<String, Bundle>> {
        return viewModel.dialogStates
    }

    fun showDialog(dialogKey: String, data: Bundle) {
        viewModel.setDialogStateValue(dialogKey, data.also { it.putBoolean(KEY_DIALOG_VISIBLE, true) })
    }

    fun quitDialog(dialogKey: String) {
        viewModel.setDialogStateValue(dialogKey, Bundle().also { it.putBoolean(KEY_DIALOG_VISIBLE, false) } )
    }

}