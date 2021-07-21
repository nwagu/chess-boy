package com.nwagu.chessboy.screens.newgame

import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nwagu.chessboy.bluetooth.BluetoothController
import com.nwagu.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.screens.navigation.Dialog
import com.nwagu.chessboy.widgets.*
import com.nwagu.bluetoothchat.ConnectionState.*
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chessboy.sharedmodels.players.BluetoothPlayer
import com.nwagu.chessboy.sharedmodels.presentation.ScanState
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColor
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NewBluetoothGameView(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navHostController: NavHostController
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val newBluetoothGameViewModel = context.newBluetoothGameViewModel

    val coroutineScope = rememberCoroutineScope()

    newBluetoothGameViewModel.onConnectSuccessHandler = { bluetoothChatService ->
        coroutineScope.launch {
            context.startNewBluetoothGame(bluetoothChatService)
            bottomSheetScaffoldState.bottomSheetState.expand()
            navHostController.navigateUp()

            // return to defaults
            newBluetoothGameViewModel.selectedColor.value = ChessPieceColor.WHITE
            newBluetoothGameViewModel.selectedDevice.value = null
        }
    }

    val connectionState by newBluetoothGameViewModel.connectState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 8.dp)
    ) {

        val bluetoothController = BluetoothController(context)

        val selectedColor by newBluetoothGameViewModel.selectedColor.collectAsState()
        val selectedDevice by newBluetoothGameViewModel.selectedDevice.collectAsState()

        val devices by newBluetoothGameViewModel.discoveredDevices.collectAsState(emptyList())
        val scanState by newBluetoothGameViewModel.scanState.collectAsState(ScanState.NONE)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            ScreenTopBar(title = "Start a new bluetooth game", navHostController)

            SubHeader(Modifier.padding(0.dp, 16.dp), text = "Choose your side")

            WrappingRow {
                RadioCard(
                    isSelected = selectedColor == ChessPieceColor.WHITE,
                    text = "White",
                    onClick = {
                        newBluetoothGameViewModel.selectedColor.value = ChessPieceColor.WHITE
                    }
                )
                RadioCard(
                    isSelected = selectedColor == ChessPieceColor.BLACK,
                    text = "Black",
                    onClick = {
                        newBluetoothGameViewModel.selectedColor.value = ChessPieceColor.BLACK
                    }
                )
            }

            Text(
                text = if (selectedColor == ChessPieceColor.WHITE)
                    "As white player, you are responsible for initiating a connection to the device playing black. Please click on SCAN to start discovering devices."
                else
                    "As black player, you will accept connection from the device playing white. Please click on RECEIVE to ensure discoverability and start listening.",
                style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 15.sp)
            )

            if (selectedColor == ChessPieceColor.WHITE) {
                Box {

                    Column {

                        Row {

                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f),
                                text = when (scanState) {
                                    ScanState.NONE -> ""
                                    ScanState.SCANNING -> "Scanning for devices..."
                                    ScanState.SCAN_FINISHED -> "Completed Scan."
                                }
                            )

                            Button(
                                onClick = {
                                    when (scanState) {
                                        ScanState.NONE, ScanState.SCAN_FINISHED -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                                                !(context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isLocationEnabled
                                            ) {
                                                navHostController.navigate(Dialog.EnableLocation.route)
                                            } else {
                                                bluetoothController.startDiscovery()
                                            }
                                        }
                                        ScanState.SCANNING -> {
                                            bluetoothController.endDiscovery()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(getPrimaryColor()))
                            ) {
                                Text(
                                    text = when (scanState) {
                                        ScanState.NONE -> "SCAN"
                                        ScanState.SCANNING -> "STOP SCAN"
                                        ScanState.SCAN_FINISHED -> "SCAN AGAIN"
                                    }
                                )
                            }
                        }

                        PlayerSelectView(
                            modifier = Modifier
                                .padding(0.dp, 16.dp, 0.dp, 16.dp),
                            players = devices.map {
                                BluetoothPlayer(
                                    name = it.name,
                                    address = it.address
                                )
                            },
                            selectedPlayer = selectedDevice,
                            onSelect = {
                                newBluetoothGameViewModel.selectedDevice.value =
                                    it as BluetoothPlayer
                            }
                        )
                    }
                }
            } else {
                Box {

                    Column {

                        Row {

                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f),
                                text = when (connectionState) {
                                    NONE -> ""
                                    LISTENING -> "Listening..."
                                    CONNECTING -> "Connecting..."
                                    CONNECTED -> "Connected"
                                }
                            )

                            Button(
                                onClick = {
                                    when (connectionState) {
                                        LISTENING -> {
                                            newBluetoothGameViewModel.bluetoothChatService.stopListening()
                                        }
                                        NONE -> {
                                            if (bluetoothController.isBluetoothEnabled) {
                                                bluetoothController.ensureDiscoverable()
                                                newBluetoothGameViewModel.listenForConnection()
                                            } else {
                                                bluetoothController.startBluetooth()
                                            }
                                        }
                                        else -> {}
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(getPrimaryColor()))
                            ) {
                                Text(
                                    text = when (connectionState) {
                                        NONE -> "RECEIVE"
                                        LISTENING -> "STOP LISTENING"
                                        else -> ""
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (selectedColor == ChessPieceColor.WHITE) {
            SubmitButton(
                modifier = Modifier
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
                    .fillMaxWidth(0.9f)
                    .padding()
                    .align(Alignment.CenterHorizontally),
                text =
                if (connectionState == CONNECTING)
                    "CONNECTING..."
                else
                    "CONNECT",
                onClick = {
                    if (connectionState == NONE) {
                        if (bluetoothController.isBluetoothEnabled) {
                            selectedDevice?.let {
                                newBluetoothGameViewModel.attemptConnectToDevice(it.address)
                            }
                        } else {
                            bluetoothController.startBluetooth()
                        }
                    }
                }
            )
        }

    }
}