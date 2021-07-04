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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.nwagu.chess.model.Board
import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.gamelogic.isOnCheck
import com.nwagu.chess.gamelogic.squareColor
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chessboy.sharedmodels.resources.colorResource
import com.nwagu.chessboy.sharedmodels.resources.getBoardBackgroundColor

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
        backgroundColor = Color(getBoardBackgroundColor()),
        elevation = 0.dp
    ) {

        val lastMove = board.movesHistory.lastOrNull()

        GridView(
            modifier = Modifier.padding(0.dp),
            numberOfColumns = board.numberOfColumns,
            items = List(board.squaresMap.count()) { it }
        ) { square ->

            val squareColor = board.squareColor(square).colorResource()

            Box(modifier = Modifier
                .fillMaxSize()
                .background(colorResource(squareColor))
                .aspectRatio(1.0f)
            ) {
                board.squaresMap[square]?.let { occupant ->
                    if (occupant is ChessPiece)
                        ChessPieceView(piece = occupant)
                }

                if (square == lastMove?.source) {
                    val color = Color.Cyan
                    Box(
                        Modifier
                            .fillMaxSize()
                            .border(1.dp, color)
                    )
                }

                if (square == lastMove?.destination) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Blue)
                    )
                }

                if ((square == board.blackKingPosition && board.isOnCheck(
                        ChessPieceColor.BLACK
                    )) ||
                    (square == board.whiteKingPosition && board.isOnCheck(
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