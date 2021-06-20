package com.nwagu.android.chessboy.widgets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.players.GUIPlayer
import com.nwagu.android.chessboy.screens.play.vm.PlayViewModel
import com.nwagu.chess.board.isStaleMate
import com.nwagu.chess.board.turn
import com.nwagu.chess.enums.ChessPieceColor

@ExperimentalAnimationApi
@Composable
fun PlayerDisplay(
    modifier: Modifier = Modifier,
    viewModel: PlayViewModel,
    color: ChessPieceColor
) {

    val boardChanged by viewModel.boardUpdated.collectAsState(0)

    val isTurn = viewModel.game.board.turn == color

    val player =
        if (color == ChessPieceColor.BLACK) viewModel.game.blackPlayer
        else viewModel.game.whitePlayer

    player as GUIPlayer

    Row(modifier = modifier
        .height(64.dp)
        .padding(16.dp, 8.dp)
        .clickable(onClick = { viewModel.getNextMove() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Invisible text to force recompose on board changed
        Text(text = boardChanged.toString(), Modifier.size(0.dp))

        Box {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(25.dp)
                    .aspectRatio(1f),
                painter = painterResource(id = player.avatar),
                contentDescription = "Player avatar",
                contentScale = ContentScale.Fit
            )
            if (isTurn) {
                Box(
                    modifier = modifier
                        .align(Alignment.BottomEnd)
                        .width(12.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
            }
        }

        if (isTurn) {
            viewModel.game.board.movesHistory.lastOrNull()?.let {
                val checkState = it.san.takeLast(1)
                if (checkState == "+") {
                    Text(
                        modifier = Modifier,
                        text = "Check",
                        color = Color.Magenta
                    )
                } else if (checkState == "#") {
                    Text(
                        modifier = Modifier,
                        text = "Checkmate",
                        color = Color.Red
                    )
                } else if (viewModel.game.board.isStaleMate()) {
                    Text(
                        modifier = Modifier,
                        text = "Stalemate",
                        color = Color.Blue
                    )
                }
            }
        }

    }
}