package com.nwagu.chessboy.screens.newgame

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.sharedmodels.players.*
import com.nwagu.chessboy.widgets.*
import com.nwagu.chess.model.ChessPieceColor
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NewGameView(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navHostController: NavHostController
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val newGameViewModel = context.newGameViewModel

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

            ScreenTopBar(title = "Start a new game against computer", navHostController)

            SubHeader(Modifier.padding(0.dp, 16.dp), text = "Choose your side")

            WrappingRow {
                RadioCard(
                    isSelected = selectedColor == null,
                    text = "Random",
                    onClick = {
                        newGameViewModel.selectedColor.value = null
                    }
                )
                RadioCard(
                    isSelected = selectedColor == ChessPieceColor.WHITE,
                    text = "White",
                    onClick = {
                        newGameViewModel.selectedColor.value = ChessPieceColor.WHITE
                    }
                )
                RadioCard(
                    isSelected = selectedColor == ChessPieceColor.BLACK,
                    text = "Black",
                    onClick = {
                        newGameViewModel.selectedColor.value = ChessPieceColor.BLACK
                    }
                )
            }

            Column {

                SubHeader(Modifier.padding(0.dp, 16.dp), text = "Select opponent")

                PlayerSelectView(
                    modifier = Modifier,
                    players = newGameViewModel.opponents,
                    selectedPlayer = selectedOpponent,
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
                newGameViewModel.getSelectedPlayers()?.let { players ->

                    context.startNewGame(players.first, players.second)

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