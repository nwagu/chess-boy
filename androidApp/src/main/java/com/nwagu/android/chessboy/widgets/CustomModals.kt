package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nwagu.android.chessboy.R
import com.nwagu.chess.enums.ChessPieceType

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
fun SelectPromotionPieceDialog(
    showDialog: Boolean,
    dismissDialog: () -> Unit,
    onSelect: ((ChessPieceType) -> Unit)?
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                dismissDialog()
            }
        ) {
            Card(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(2.dp),
                backgroundColor = Color.White,
                border = BorderStroke(1.dp, Color.Black),
                elevation = 0.dp
            ) {
                Column {

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Promote to:",
                        style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(onClick = {
                                    onSelect?.invoke(ChessPieceType.QUEEN)
                                    dismissDialog()
                                }),
                            painter = painterResource(R.drawable.img_white_queen),
                            contentDescription = "white_queen"
                        )
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(onClick = {
                                    onSelect?.invoke(ChessPieceType.KNIGHT)
                                    dismissDialog()
                                }),
                            painter = painterResource(R.drawable.img_white_knight),
                            contentDescription = "white_knight"
                        )
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(onClick = {
                                    onSelect?.invoke(ChessPieceType.BISHOP)
                                    dismissDialog()
                                }),
                            painter = painterResource(R.drawable.img_white_bishop),
                            contentDescription = "white_bishop"
                        )
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(onClick = {
                                    onSelect?.invoke(ChessPieceType.ROOK)
                                    dismissDialog()
                                }),
                            painter = painterResource(R.drawable.img_white_rook),
                            contentDescription = "white_rook"
                        )
                    }
                }
            }
        }
    }
}