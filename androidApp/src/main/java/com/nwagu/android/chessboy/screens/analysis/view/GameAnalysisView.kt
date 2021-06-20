package com.nwagu.android.chessboy.screens.analysis.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.ui.data.ScreenConfig
import com.nwagu.android.chessboy.screens.analysis.vm.GameAnalysisViewModel
import com.nwagu.android.chessboy.screens.main.view.MainActivity
import com.nwagu.android.chessboy.widgets.*

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun GameAnalysisView(
    navHostController: NavHostController,
    dialogController: DialogController,
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val gameAnalysisViewModel = context.gameAnalysisViewModel

    Column(modifier = Modifier
        .background(color = Color.White)
        .fillMaxSize()
        .padding(16.dp, 8.dp)
    ) {

        ScreenTopBar(title = "Analysis", navHostController)

        Column(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            SubHeader(text = "${gameAnalysisViewModel.game.whitePlayer.name} vs ${gameAnalysisViewModel.game.blackPlayer.name}")

            ChessBoardThumbView(board = gameAnalysisViewModel.game.board)

            SimpleFlowRow {
                for (move in gameAnalysisViewModel.game.board.movesHistory) {
                    Text(
                        modifier = Modifier.padding(8.dp).background(Color.Green),
                        text = move.san
                    )
                }
            }

        }

    }

}