package com.nwagu.android.chessboy.widgets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.players.User
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.android.chessboy.util.colorResource
import com.nwagu.chess.Game
import com.nwagu.chess.board.Board
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.board.isOnCheck
import com.nwagu.chess.board.squareColor
import com.nwagu.chess.enums.ChessPieceColor

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChessBoardStatic(
    modifier: Modifier = Modifier,
    board: Board
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(2.dp),
        backgroundColor = AppColor.boardBackground,
        elevation = 0.dp
    ) {

        val lastMove = board.movesHistory.lastOrNull()

        GridView(
            modifier = Modifier.padding(0.dp),
            numberOfColumns = board.numberOfColumns,
            items = List(board.squaresMap.count()) { it }
        ) { cellPosition ->

            val cellColor = board.squareColor(cellPosition).colorResource()

            Box(modifier = Modifier
                .fillMaxSize()
                .background(cellColor)
                .aspectRatio(1.0f)
            ) {
                board.squaresMap[cellPosition]?.let { occupant ->
                    if (occupant is ChessPiece)
                        ChessPieceView(piece = occupant)
                }

                if (cellPosition == lastMove?.source) {
                    val color = Color.Cyan
                    Box(
                        Modifier
                            .fillMaxSize()
                            .border(1.dp, color)
                    )
                }

                if (cellPosition == lastMove?.destination) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Blue)
                    )
                }

                if ((cellPosition == board.blackKingPosition && board.isOnCheck(
                        ChessPieceColor.BLACK
                    )) ||
                    (cellPosition == board.whiteKingPosition && board.isOnCheck(
                        ChessPieceColor.WHITE
                    ))
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .border(2.dp, Color.Red)
                    )
                }

            }
        }
    }
}