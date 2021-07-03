package com.nwagu.android.chessboy.widgets

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.screens.navigation.Dialog
import com.nwagu.android.chessboy.screens.play.vm.PlayViewModel
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.gamelogic.isOnCheck
import com.nwagu.chess.gamelogic.squareColor
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.Promotion
import com.nwagu.chessboy.sharedmodels.utils.colorOnUserSideOfBoard
import com.nwagu.chessboy.sharedmodels.utils.colorResource
import com.nwagu.chessboy.sharedmodels.utils.imageRes
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ChessBoardView(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModel: PlayViewModel
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

                val square = if (viewModel.game.colorOnUserSideOfBoard == ChessPieceColor.WHITE)
                    it
                else
                    (board.numberOfColumns * board.numberOfRows) - (it + 1)

                val squareColor = board.squareColor(square).colorResource()

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(squareColor))
                    .aspectRatio(1.0f)
                    .clickable(
                        onClick = {
                            val move = viewModel.squareClicked(square)

                            if (move is Promotion) {
                                // Handle promotion piece selection
                                val moveJson = Json.encodeToString(move)
                                navHostController.navigate(Dialog.SelectPromotionPiece.route + "/$moveJson")
                            }
                        }
                    )
                ) {
                    board.squaresMap[square]?.let { occupant ->
                        if (occupant is ChessPiece)
                            ChessPieceView(piece = occupant)

                        AnimatedVisibility(
                            visible = (square in possibleMoves.map { it.destination }),
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