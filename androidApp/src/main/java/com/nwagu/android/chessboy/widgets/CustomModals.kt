package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nwagu.android.chessboy.R

@Composable
fun AlertDialogWrapper(
    showDialog: Boolean,
    dismissDialog: () -> Unit,
    cancellable: Boolean = true,
    title: String = "",
    message: String = "",
    confirmMessage: String = "Ok",
    dismissMessage: String = "Dismiss",
    confirmAction: () -> Unit = {},
    dismissAction: () -> Unit = {}
) {
    if (showDialog) {
        AlertDialog(
            title = { Text(title, style = TextStyle(fontWeight = FontWeight.Bold)) },
            text = { Text(message) },
            onDismissRequest = {
                if (cancellable) {
                    dismissDialog()
                    dismissAction()
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
                    style = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
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
                    style = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
            }
        )
    }
}


@Composable
fun GameVsComputerInitDialog(
    showDialog: Boolean,
    dismissDialog: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                dismissDialog()
            }
        ) {
            Card(modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(2.dp),
                backgroundColor = Color.White,
                elevation = 0.dp
            ) {
                Row {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.img_white_king),
                            contentDescription = "white_king"
                        )
                        Text(text = "Play as White")
                    }
                    Column {
                        Image(
                            painter = painterResource(R.drawable.img_black_king),
                            contentDescription = "black_king"
                        )
                        Text(text = "Play as Black")
                    }
                }
            }
        }
    }
}