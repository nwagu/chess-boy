package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.util.imageRes
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.enums.ChessPieceColor

@Composable
fun CaptivesView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    color: ChessPieceColor
) {

    val boardChanged by viewModel.boardUpdated.collectAsState(0)

    Box(modifier = modifier) {

        // Invisible text to force recompose on board changed
        Text(text = boardChanged.toString(), Modifier.size(0.dp))

        SimpleFlowRow(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(32.dp)
        ) {
            viewModel.game.board.captives.filter { it.chessPieceColor == color }.forEach {
                CapturedPieceView(it)
            }
        }
    }
}

@Composable
fun CapturedPieceView(piece: ChessPiece) {
    Image(
        painter = painterResource(piece.imageRes()),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxHeight()
    )
}