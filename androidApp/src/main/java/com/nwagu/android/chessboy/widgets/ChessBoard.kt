package com.nwagu.android.chessboy.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.colorResource
import com.nwagu.android.chessboy.movesgenerators.User
import com.nwagu.android.chessboy.screens.ChessPieceView
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.chess.Game
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.board.isOnCheck
import com.nwagu.chess.board.squareColor
import com.nwagu.chess.enums.ChessPieceColor

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChessBoardView(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel
) {

    Card(modifier = modifier,
        shape = RoundedCornerShape(2.dp),
        backgroundColor = AppColor.boardBackground,
        elevation = 0.dp
    ) {

        Box(modifier = Modifier.padding(0.dp)) {

            val boardChanged by viewModel.boardUpdated.collectAsState(0)
            val board = viewModel.game.board
            val possibleMoves by viewModel.possibleMoves.collectAsState(emptyList())
            val lastMove = board.movesHistory.lastOrNull()

            // Invisible text to force recompose on board changed
            Text(text = boardChanged.toString(), Modifier.size(0.dp))

            GridView(
                modifier = Modifier.padding(0.dp),
                numberOfColumns = board.numberOfColumns,
                items = List(board.squaresMap.count()) { it }
            ) {

                val cellPosition = if (viewModel.game.blackPlayer == User)
                    (board.numberOfColumns * board.numberOfRows) - (it + 1)
                else
                    it

                val cellColor = board.squareColor(cellPosition).colorResource()

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(cellColor)
                    .aspectRatio(1.0f)
                    .clickable(
                        onClick = {
                            viewModel.cellClicked(cellPosition)
                        }
                    )
                ) {
                    board.squaresMap[cellPosition]?.let { occupant ->
                        if (occupant is ChessPiece)
                            ChessPieceView(piece = occupant)

                        AnimatedVisibility(
                            visible = (cellPosition in possibleMoves.map { it.destination }),
                            modifier = Modifier
                                .fillMaxSize(0.3f)
                                .align(Alignment.Center),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            val color =
                                if (occupant is ChessPiece) Color.Red else Color.LightGray
                            Box(
                                Modifier
                                    .clip(CircleShape)
                                    .background(color)
                            )
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
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChessBoardThumbView(
    modifier: Modifier = Modifier,
    game: Game
) {

    Card(modifier = modifier,
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