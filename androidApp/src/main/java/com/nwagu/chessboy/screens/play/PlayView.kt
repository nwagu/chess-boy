package com.nwagu.chessboy.screens.play

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.chessboy.bluetooth.BluetoothController
import com.nwagu.chessboy.model.ViewAction
import com.nwagu.chessboy.model.ScreenConfig
import com.nwagu.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.widgets.ChessBoardView
import com.nwagu.chessboy.widgets.LightActionView
import com.nwagu.chessboy.widgets.PlayerDisplay
import com.nwagu.bluetoothchat.ConnectionState.*
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.opposite
import com.nwagu.chessboy.screens.navigation.Dialog
import com.nwagu.chessboy.sharedmodels.players.BluetoothPlayer
import com.nwagu.chessboy.sharedmodels.presentation.PlayViewModel
import com.nwagu.chessboy.sharedmodels.utils.colorOnUserSideOfBoard
import com.nwagu.chessboy.sharedmodels.utils.isBluetoothGame
import com.nwagu.chessboy.sharedmodels.utils.userColor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun PlayView(
    navHostController: NavHostController
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val viewModel = context.playViewModel
    val bluetoothController = BluetoothController(context)

    val gameChanged by viewModel.gameUpdated.collectAsState(0)

    val pendingPromotion by viewModel.pendingPromotion.collectAsState(null)

    if (pendingPromotion != null) {
        val moveJson = Json.encodeToString(pendingPromotion)
        navHostController.navigate(Dialog.SelectPromotionPiece.route + "/$moveJson")
        viewModel.pendingPromotion.value = null
    }

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

        val gameActions = mutableListOf(ViewAction("Undo") { viewModel.undo() })
        if (viewModel.game.isBluetoothGame()) {
            gameActions.add(ViewAction("Reconnect") {

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
            PlayViewPortrait(viewModel, navHostController, gameActions)
        else
            PlayViewLandscape(viewModel, screenConfig, navHostController, gameActions)
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun PlayViewPortrait(
    viewModel: PlayViewModel,
    navHostController: NavHostController,
    gameActions: List<ViewAction>
) {

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        PlayerDisplay(modifier = Modifier, viewModel, viewModel.game.colorOnUserSideOfBoard.opposite())
        ChessBoardView(modifier = Modifier.fillMaxWidth(), viewModel)
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
fun PlayViewLandscape(
    viewModel: PlayViewModel,
    screenConfig: ScreenConfig,
    navHostController: NavHostController,
    gameActions: List<ViewAction>
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
            PlayerDisplay(
                modifier = Modifier,
                viewModel, viewModel.game.colorOnUserSideOfBoard)
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
                viewModel
            )
        }

    }
}
