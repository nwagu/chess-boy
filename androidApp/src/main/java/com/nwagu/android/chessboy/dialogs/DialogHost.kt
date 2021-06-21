package com.nwagu.android.chessboy.dialogs

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.nwagu.android.chessboy.dialogs.DialogStateViewModel.Companion.KEY_DIALOG_VISIBLE
import com.nwagu.android.chessboy.screens.main.view.MainActivity
import com.nwagu.android.chessboy.screens.model.Dialog
import com.nwagu.android.chessboy.widgets.AlertDialogWrapper
import com.nwagu.android.chessboy.widgets.SelectPromotionPieceDialog
import com.nwagu.chess.enums.ChessPieceType

const val KEY_PROMOTION_PIECE = "onselect_promo"

@Composable
fun DialogHost(
    dialogController: DialogController
) {

    val dialogStates by dialogController.getStates().collectAsState(hashMapOf())

    val context = LocalContext.current as MainActivity

    fun getVisibility(key: String): Boolean {
        return dialogStates[key]?.getBoolean(KEY_DIALOG_VISIBLE) ?: false
    }

    fun getData(dialogKey: String, dataKey: String): Any? {
        return dialogStates[dialogKey]?.get(dataKey)
    }

    fun removeDialog(key: String) {
        dialogController.quitDialog(key)
    }

    SelectPromotionPieceDialog(
        getVisibility(Dialog.SelectPromotionPiece.id),
        { removeDialog(Dialog.SelectPromotionPiece.id) },
        getData(Dialog.SelectPromotionPiece.id, KEY_PROMOTION_PIECE) as ((ChessPieceType) -> Unit)?
    )

    AlertDialogWrapper(showDialog = getVisibility(Dialog.EnableLocationDialog.id),
        dismissDialog = { removeDialog(Dialog.EnableLocationDialog.id) },
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