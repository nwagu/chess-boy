package com.nwagu.android.chessboy.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nwagu.android.chessboy.dialogs.DialogStateViewModel.Companion.KEY_DIALOG_VISIBLE
import com.nwagu.android.chessboy.screens.Dialog
import com.nwagu.android.chessboy.widgets.SelectPromotionPieceDialog
import com.nwagu.chess.enums.ChessPieceType

const val KEY_PROMOTION_PIECE = "onselectpromo"

@Composable
fun DialogHost(
    dialogController: DialogController
) {

    val dialogStates by dialogController.getStates().collectAsState(hashMapOf())

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

}