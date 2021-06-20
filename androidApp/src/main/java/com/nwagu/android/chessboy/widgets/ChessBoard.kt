package com.nwagu.android.chessboy.widgets

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.dialogs.KEY_PROMOTION_PIECE
import com.nwagu.android.chessboy.screens.Dialog
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.android.chessboy.util.colorResource
import com.nwagu.android.chessboy.util.imageRes
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.util.colorOnUserSideOfBoard
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.board.isOnCheck
import com.nwagu.chess.board.squareColor
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.moves.Promotion
import java.io.Serializable

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChessBoardView(
    modifier: Modifier = Modifier,
    dialogController: DialogController,
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

                val cellPosition = if (viewModel.game.colorOnUserSideOfBoard == ChessPieceColor.WHITE)
                    it
                else
                    (board.numberOfColumns * board.numberOfRows) - (it + 1)

                val cellColor = board.squareColor(cellPosition).colorResource()

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(cellColor)
                    .aspectRatio(1.0f)
                    .clickable(
                        onClick = {

                            val move = viewModel.squareClicked(cellPosition)

                            // Handle promotion piece selection
                            if (move is Promotion) {
                                dialogController.showDialog(
                                    Dialog.SelectPromotionPiece.id,
                                    Bundle().apply {
                                        putSerializable(
                                            KEY_PROMOTION_PIECE,
                                            { selectedType: ChessPieceType ->
                                                move.promotionType = selectedType
                                                viewModel.makeUserMove(move)
                                            } as Serializable)
                                    }
                                )
                            }
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

@Composable
fun ChessPieceView(piece: ChessPiece) {
    Image(
        painter = painterResource(piece.imageRes()),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
    )
}