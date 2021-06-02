package com.nwagu.android.chessboy.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nwagu.android.chessboy.screens.Dialog
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.widgets.AlertDialogWrapper

@Composable
fun DialogHost(
    dialogController: DialogController,
    gameViewModel: GameViewModel
) {

    val dialogStates by dialogController.getStates().collectAsState(hashMapOf())

    fun getState(key: String): Boolean {
        return dialogStates[key] ?: false
    }

    fun quitDialog(key: String) {
        dialogController.quitDialog(key)
    }

    AlertDialogWrapper(getState(Dialog.QuitGame.id), { quitDialog(Dialog.QuitGame.id) },
        title = "Quit Game",
        message = "Are you sure you wish to quit?",
        confirmMessage = "Quit",
        dismissMessage = "Back to game",
        cancellable = true,
        confirmAction = {},
        dismissAction = {}
    )

    AlertDialogWrapper(getState(Dialog.ResignGame.id), { quitDialog(Dialog.ResignGame.id) },
        title = "Resign Game",
        message = "Are you sure you wish to resign? This will be saved as a loss.",
        confirmMessage = "Resign",
        dismissMessage = "Back to game",
        cancellable = true,
        confirmAction = {},
        dismissAction = {}
    )

}