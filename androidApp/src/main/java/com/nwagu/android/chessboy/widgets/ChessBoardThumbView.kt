package com.nwagu.android.chessboy.widgets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.players.User
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.android.chessboy.util.colorResource
import com.nwagu.chess.Game
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.board.squareColor

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChessBoardThumbView(
    modifier: Modifier = Modifier,
    game: Game
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(2.dp),
        backgroundColor = AppColor.boardBackground,
        elevation = 0.dp
    ) {

        GridView(
            modifier = Modifier.padding(0.dp),
            numberOfColumns = game.board.numberOfColumns,
            items = List(game.board.squaresMap.count()) { it }
        ) {

            val cellPosition = if (game.blackPlayer == User)
                (game.board.numberOfColumns * game.board.numberOfRows) - (it + 1)
            else
                it

            val cellColor = game.board.squareColor(cellPosition).colorResource()

            Box(modifier = Modifier
                .fillMaxSize()
                .background(cellColor)
                .aspectRatio(1.0f)
            ) {
                game.board.squaresMap[cellPosition]?.let { occupant ->
                    if (occupant is ChessPiece)
                        ChessPieceView(piece = occupant)
                }

            }
        }
    }
}