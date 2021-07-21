package com.nwagu.chessboy.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColorDark

@Composable
fun AlertDialogWrapper(
    dismissDialog: () -> Unit,
    cancellable: Boolean = true,
    title: String = "",
    message: String = "",
    confirmMessage: String = "Ok",
    dismissMessage: String = "Dismiss",
    confirmAction: () -> Unit = {},
    dismissAction: () -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        title = { Text(title, style = TextStyle(fontWeight = FontWeight.Bold)) },
        text = { Text(message) },
        onDismissRequest = {
            if (cancellable) {
                dismissDialog()
            }
        },
        confirmButton = {
            Text(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .clickable(
                        onClick = {
                            confirmAction()
                            dismissDialog()
                        }
                    ),
                text = confirmMessage,
                style = TextStyle(color = colorResource(getPrimaryColorDark()), fontWeight = FontWeight.Bold))
        },
        dismissButton = {
            Text(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .clickable(
                        onClick = {
                            dismissAction()
                            dismissDialog()
                        }
                    ),
                text = dismissMessage,
                style = TextStyle(color = colorResource(getPrimaryColorDark()), fontWeight = FontWeight.Bold))
        }
    )
}
