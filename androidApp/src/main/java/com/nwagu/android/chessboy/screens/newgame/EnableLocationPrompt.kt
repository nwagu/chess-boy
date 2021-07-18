package com.nwagu.android.chessboy.screens.newgame

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.util.Utils
import com.nwagu.android.chessboy.widgets.AlertDialogWrapper

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