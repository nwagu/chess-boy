package com.nwagu.android.chessboy.screens

import android.content.Context
import android.location.LocationManager
import android.os.Build
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.bluetooth.BluetoothController
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.ui.data.ScreenConfig
import com.nwagu.android.chessboy.players.BluetoothPlayer
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.vm.NewBluetoothGameViewModel
import com.nwagu.android.chessboy.vm.ScanState
import com.nwagu.android.chessboy.widgets.*
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NewBluetoothGameView(
    gameViewModel: GameViewModel,
    newBluetoothGameViewModel: NewBluetoothGameViewModel,
    screenConfig: ScreenConfig,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navHostController: NavHostController,
    dialogController: DialogController,
) {

    val coroutineScope = rememberCoroutineScope()

    newBluetoothGameViewModel.onConnectSuccessHandler = { bluetoothChatService ->
        coroutineScope.launch {
            gameViewModel.startNewBluetoothGame(bluetoothChatService)
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

        val context = LocalContext.current
        val bluetoothController = BluetoothController(context as MainActivity)

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

            SimpleFlowRow {
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
                                                dialogController.showDialog(Dialog.EnableLocationDialog.id)
                                            } else {
                                                bluetoothController.startDiscovery()
                                            }
                                        }
                                        ScanState.SCANNING -> {
                                            bluetoothController.endDiscovery()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.Primary)
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

                        OpponentSelect(
                            modifier = Modifier
                                .padding(0.dp, 16.dp, 0.dp, 16.dp),
                            items = devices.map {
                                BluetoothPlayer(
                                    name = it.name,
                                    address = it.address
                                )
                            },
                            selectedItem = selectedDevice,
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
                                    BluetoothChatService.ConnectionState.NONE -> ""
                                    BluetoothChatService.ConnectionState.LISTENING -> "Listening..."
                                    BluetoothChatService.ConnectionState.CONNECTING -> "Connecting..."
                                    BluetoothChatService.ConnectionState.CONNECTED -> "Connected"
                                }
                            )

                            Button(
                                onClick = {
                                    when (connectionState) {
                                        BluetoothChatService.ConnectionState.LISTENING -> {
                                            newBluetoothGameViewModel.bluetoothChatService.stopListening()
                                        }
                                        BluetoothChatService.ConnectionState.NONE -> {
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
                                colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.Primary)
                            ) {
                                Text(
                                    text = when (connectionState) {
                                        BluetoothChatService.ConnectionState.NONE -> "RECEIVE"
                                        BluetoothChatService.ConnectionState.LISTENING -> "STOP LISTENING"
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
                if (connectionState == BluetoothChatService.ConnectionState.CONNECTING)
                    "CONNECTING..."
                else
                    "CONNECT",
                onClick = {
                    if (connectionState == BluetoothChatService.ConnectionState.NONE) {
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