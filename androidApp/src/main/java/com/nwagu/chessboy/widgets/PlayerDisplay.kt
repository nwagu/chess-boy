package com.nwagu.chessboy.widgets

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
import com.nwagu.chess.gamelogic.turn
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chessboy.sharedmodels.players.GUIPlayer
import com.nwagu.chessboy.sharedmodels.presentation.PlayViewModel
import com.nwagu.chessboy.sharedmodels.utils.checkMessageForNextPlayer

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
            Text(
                modifier = Modifier,
                text = viewModel.game.board.checkMessageForNextPlayer(),
                color = Color.Magenta
            )
        }

    }
}