package com.nwagu.chessboy.screens.analysis

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.nwagu.chessboy.util.Utils
import com.nwagu.chessboy.widgets.AlertDialogWrapper

@Composable
fun ConfirmDeletePrompt(
    navHostController: NavHostController,
    id: Long
) {

    val context = Utils.unwrap(LocalContext.current)
    val mainViewModel = context.mainViewModel

    AlertDialogWrapper(
        dismissDialog = { navHostController.navigateUp() },
        cancellable = true,
        title = "Confirm delete",
        message = "Delete this game?",
        confirmMessage = "Delete",
        dismissMessage = "Cancel",
        confirmAction = {
            mainViewModel.gamesHistoryRepository.deleteGame(id)
            navHostController.navigateUp()
        },
        dismissAction = {}
    )
}