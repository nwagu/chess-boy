package com.nwagu.chessboy.screens.history

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nwagu.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.screens.navigation.Screen
import com.nwagu.chessboy.widgets.PreviousGameView
import com.nwagu.chessboy.widgets.ScreenTopBar

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun HistoryView(
    navHostController: NavHostController
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val mainViewModel = context.mainViewModel

    val gamesHistory = mainViewModel.getGamesHistory()

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

            if (gamesHistory.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No history to show",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 15.sp)
                )
            } else {
                for (game in gamesHistory) {
                    PreviousGameView(modifier = Modifier.fillMaxWidth(), game.pgn) {
                        context.gameAnalysisViewModel.savedGame = game
                        navHostController.navigate(Screen.GameAnalysis.route)
                    }
                }
            }
        }
    }

}