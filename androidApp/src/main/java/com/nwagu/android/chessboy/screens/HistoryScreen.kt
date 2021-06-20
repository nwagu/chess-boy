package com.nwagu.android.chessboy.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.ui.data.ScreenConfig
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.widgets.PreviousGameView
import com.nwagu.android.chessboy.widgets.ScreenTopBar

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun HistoryView(
    gameViewModel: GameViewModel,
    screenConfig: ScreenConfig,
    navHostController: NavHostController,
    dialogController: DialogController,
) {

    val gamesHistory = gameViewModel.getGamesHistory()

    Column(modifier = Modifier
        .background(color = Color.White)
        .fillMaxSize()
        .padding(16.dp, 8.dp)
    ) {

        ScreenTopBar(title = "History", navHostController)

        Column(modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {

            for (gamePgn in gamesHistory.asReversed()) {
                PreviousGameView(modifier = Modifier.fillMaxWidth(), gamePgn) {
                    // onclick
                }
            }
        }
    }

}