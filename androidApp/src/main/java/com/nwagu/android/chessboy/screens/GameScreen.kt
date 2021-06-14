package com.nwagu.android.chessboy.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.BluetoothController
import com.nwagu.android.chessboy.MainActivity
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.model.data.LightAction
import com.nwagu.android.chessboy.model.data.ScreenConfig
import com.nwagu.android.chessboy.players.BluetoothPlayer
import com.nwagu.android.chessboy.util.imageRes
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.vm.colorOnUserSideOfBoard
import com.nwagu.android.chessboy.vm.isBluetoothGame
import com.nwagu.android.chessboy.vm.userColor
import com.nwagu.android.chessboy.widgets.ChessBoardView
import com.nwagu.android.chessboy.widgets.LightActionView
import com.nwagu.android.chessboy.widgets.SimpleFlowRow
import com.nwagu.bluetoothchat.BluetoothChatService.ConnectionState.*
import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.board.isStaleMate
import com.nwagu.chess.board.turn
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.opposite

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun GameView(
    viewModel: GameViewModel,
    screenConfig: ScreenConfig,
    navHostController: NavHostController,
    dialogController: DialogController
) {

    val gameChanged by viewModel.gameUpdated.collectAsState(0)

    val context = LocalContext.current
    val bluetoothController = BluetoothController(context as MainActivity)

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxHeight()
    ) {

        val boardChanged by viewModel.boardUpdated.collectAsState(0)

        // Invisible text to force recompose on game changed
        Text(text = gameChanged.toString(), Modifier.size(0.dp))

        Card(
            Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(0.dp),
            backgroundColor = Color.White,
            elevation = 4.dp
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {

                // Invisible text to force recompose on board changed
                Text(text = boardChanged.toString(), Modifier.size(0.dp))

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    text = viewModel.game.title,
                    style = TextStyle(Color.Black, fontWeight = FontWeight.Bold)
                )

                if (viewModel.game.isBluetoothGame()) {

                    val bluetoothConnectionState by viewModel.bluetoothChatService.connectionState.collectAsState()

                    AnimatedVisibility(
                        visible = (true),
                        modifier = Modifier
                            .padding(16.dp, 8.dp)
                            .width(12.dp)
                            .height(12.dp),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {

                        val color = when (bluetoothConnectionState) {
                            NONE -> Color.Black
                            LISTENING -> Color.Black
                            CONNECTING -> Color.Red
                            CONNECTED -> Color.Green
                        }

                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }
        }

        val isLandscape = (screenConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)

        val gameActions = mutableListOf(LightAction("Undo") { viewModel.undo() })
        if (viewModel.game.isBluetoothGame()) {
            gameActions.add(LightAction("Reconnect") {

                if (bluetoothController.isBluetoothEnabled) {

                    if (viewModel.game.userColor == ChessPieceColor.WHITE)
                        viewModel.attemptReconnectToDevice((viewModel.game.blackPlayer as BluetoothPlayer).address)
                    else {
                        bluetoothController.ensureDiscoverable()
                        viewModel.listenForReconnection()
                    }
                } else {
                    bluetoothController.startBluetooth()
                }

            })
        }

        if (!isLandscape)
            GameViewPortrait(viewModel, navHostController, dialogController, gameActions)
        else
            GameViewLandscape(viewModel, screenConfig, navHostController, dialogController, gameActions)
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun GameViewPortrait(
    viewModel: GameViewModel,
    navHostController: NavHostController,
    dialogController: DialogController,
    gameActions: List<LightAction>
) {

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        PlayerDisplay(modifier = Modifier, viewModel, viewModel.game.colorOnUserSideOfBoard.opposite())
        // CaptivesView(modifier = Modifier.fillMaxWidth(), viewModel, viewModel.game.colorOnUserSideOfBoard)
        ChessBoardView(modifier = Modifier.fillMaxWidth(), dialogController, viewModel)
        // CaptivesView(modifier = Modifier.fillMaxWidth(), viewModel, viewModel.game.colorOnUserSideOfBoard.opposite())
        PlayerDisplay(modifier = Modifier, viewModel, viewModel.game.colorOnUserSideOfBoard)
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)) {
            for (action in gameActions) {
                LightActionView(action)
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun GameViewLandscape(
    viewModel: GameViewModel,
    screenConfig: ScreenConfig,
    navHostController: NavHostController,
    dialogController: DialogController,
    gameActions: List<LightAction>
) {



    Row(modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            PlayerDisplay(
                modifier = Modifier,
                viewModel, viewModel.game.colorOnUserSideOfBoard.opposite())
            // CaptivesView(modifier = Modifier.fillMaxWidth(), viewModel, viewModel.game.colorOnUserSideOfBoard)
            PlayerDisplay(
                modifier = Modifier,
                viewModel, viewModel.game.colorOnUserSideOfBoard)
            // CaptivesView(modifier = Modifier.fillMaxWidth(), viewModel, viewModel.game.colorOnUserSideOfBoard.opposite())
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)) {
                for (action in gameActions) {
                    LightActionView(action)
                }
            }
        }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            ChessBoardView(
                modifier = Modifier.width((screenConfig.screenWidthDp - 64).dp),
                dialogController,
                viewModel
            )
        }

    }
}

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

@Composable
fun CapturedPieceView(piece: ChessPiece) {
    Image(
        painter = painterResource(piece.imageRes()),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxHeight()
    )
}

@ExperimentalAnimationApi
@Composable
fun PlayerDisplay(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel,
    color: ChessPieceColor) {

    val boardChanged by viewModel.boardUpdated.collectAsState(0)

    val isTurn = viewModel.game.board.turn == color

    val player =
        if (color == ChessPieceColor.BLACK) viewModel.game.blackPlayer
        else viewModel.game.whitePlayer

    Row(modifier = modifier
        .height(64.dp)
        .padding(16.dp, 8.dp)
        .clickable(onClick = { viewModel.getNextMove() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Invisible text to force recompose on board changed
        Text(text = boardChanged.toString(), Modifier.size(0.dp))

        Text(
            modifier = Modifier,
            text = player.name,
            color = Color.Black
        )

        AnimatedVisibility(
            visible = (isTurn),
            modifier = Modifier
                .width(16.dp)
                .height(16.dp),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Color.Green))
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