package com.nwagu.chessboy.screens.newgame

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.nwagu.chessboy.util.Utils
import com.nwagu.chessboy.widgets.AlertDialogWrapper

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun EnableLocationPrompt(
    navHostController: NavHostController,
) {

    val context = Utils.unwrap(LocalContext.current)

    AlertDialogWrapper(
        dismissDialog = { navHostController.navigateUp() },
        cancellable = true,
        title = "Location services required",
        message = "Please enable location services in settings to start discovery.",
        confirmMessage = "Settings",
        dismissMessage = "Dismiss",
        confirmAction = {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        },
        dismissAction = {}
    )
}