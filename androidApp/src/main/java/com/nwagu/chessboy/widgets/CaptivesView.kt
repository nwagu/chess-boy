package com.nwagu.chessboy.widgets

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
import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chessboy.sharedmodels.presentation.PlayViewModel
import com.nwagu.chessboy.sharedmodels.resources.imageRes

@Composable
fun CaptivesView(
    modifier: Modifier = Modifier,
    viewModel: PlayViewModel,
    color: ChessPieceColor
) {

    val boardChanged by viewModel.boardUpdated.collectAsState(0)

    Box(modifier = modifier) {

        // Invisible text to force recompose on board changed
        Text(text = boardChanged.toString(), Modifier.size(0.dp))

        WrappingRow(modifier = Modifier
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