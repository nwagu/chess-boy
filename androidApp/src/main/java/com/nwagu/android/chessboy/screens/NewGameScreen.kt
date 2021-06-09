package com.nwagu.android.chessboy.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.model.data.ScreenConfig
import com.nwagu.android.chessboy.players.MoveGenerator
import com.nwagu.android.chessboy.players.User
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.vm.NewGameViewModel
import com.nwagu.android.chessboy.widgets.*
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NewGameView(
    gameViewModel: GameViewModel,
    newGameViewModel: NewGameViewModel,
    screenConfig: ScreenConfig,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navHostController: NavHostController,
    dialogController: DialogController
) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 8.dp)
    ) {

        val selectedColor by newGameViewModel.selectedColor.collectAsState()
        val selectedOpponent by newGameViewModel.selectedOpponent.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        navHostController.navigateUp()
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Close", tint = Color.Black)
                }

                Header(Modifier.padding(0.dp, 8.dp), text = "Start a new game with computer")
            }

            SubHeader(Modifier.padding(0.dp, 16.dp), text = "Choose Color")

            SimpleFlowRow {
                RadioCard(
                    selected = selectedColor == null,
                    text = "Random",
                    onClick = {
                        newGameViewModel.selectedColor.value = null
                    }
                )
                RadioCard(
                    selected = selectedColor == ChessPieceColor.WHITE,
                    text = "White",
                    onClick = {
                        newGameViewModel.selectedColor.value = ChessPieceColor.WHITE
                    }
                )
                RadioCard(
                    selected = selectedColor == ChessPieceColor.BLACK,
                    text = "Black",
                    onClick = {
                        newGameViewModel.selectedColor.value = ChessPieceColor.BLACK
                    }
                )
            }

            Column {

                SubHeader(Modifier.padding(0.dp, 16.dp), text = "Select Opponent")

                OpponentSelect(
                    modifier = Modifier,
                    items = newGameViewModel.opponents,
                    selectedItem = selectedOpponent,
                    onSelect = {
                        newGameViewModel.selectedOpponent.value = it as MoveGenerator
                    }
                )

            }

        }

        SubmitButton(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            text = "Start Game",
            onClick = {
                selectedOpponent?.let { opponent ->
                    val whitePlayer = when (selectedColor) {
                        ChessPieceColor.WHITE -> User
                        ChessPieceColor.BLACK -> opponent
                        null -> listOf(User, opponent).random()
                    }
                    val blackPlayer = if (whitePlayer is User) opponent else User
                    gameViewModel.startNewGame(whitePlayer, blackPlayer)

                    newGameViewModel.selectedColor.value = null
                    newGameViewModel.selectedOpponent.value = null

                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                        navHostController.navigateUp()
                    }
                }
            }
        )

    }
}