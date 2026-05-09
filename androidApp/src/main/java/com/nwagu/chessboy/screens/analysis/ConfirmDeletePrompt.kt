package com.nwagu.chessboy.screens.analysis

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.nwagu.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.widgets.AlertDialogWrapper

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ConfirmDeletePrompt(
    navHostController: NavHostController,
    id: Long
) {

    val context = LocalActivity.current as MainActivity
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