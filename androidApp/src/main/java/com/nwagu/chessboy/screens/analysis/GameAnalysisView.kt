package com.nwagu.chessboy.screens.analysis

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.model.LightAction
import com.nwagu.chessboy.screens.navigation.Dialog
import com.nwagu.chessboy.widgets.*

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun GameAnalysisView(
    navHostController: NavHostController
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val gameAnalysisViewModel = context.gameAnalysisViewModel

    val boardChanged by gameAnalysisViewModel.boardUpdated.collectAsState(0)

    Column(modifier = Modifier
        .background(color = Color.White)
        .fillMaxSize()
        .padding(16.dp, 8.dp)
    ) {

        ScreenTopBar(title = "Game review", navHostController)

        // Invisible text to force recompose on board changed
        Text(text = boardChanged.toString(), Modifier.size(0.dp))

        val isLandscape = (screenConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)

        val navActions = mutableListOf(
            LightAction("First") {
                gameAnalysisViewModel.first()
            },
            LightAction("Prev") {
                gameAnalysisViewModel.previous()
            },
            LightAction("Next") {
                gameAnalysisViewModel.next()
            },
            LightAction("Last") {
                gameAnalysisViewModel.last()
            }
        )

        val otherActions = mutableListOf(
            LightAction("Share") {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, gameAnalysisViewModel.savedGame?.pgn)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)

            },
            LightAction("Delete") {
                navHostController.navigate(
                    Dialog.ConfirmDeleteGame.route +
                            "/${gameAnalysisViewModel.savedGame?.id}"
                )
            }
        )

        if (!isLandscape)
            GameAnalysisViewPortrait(navActions, otherActions)
        else
            GameAnalysisViewLandscape(navActions, otherActions)

    }

}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun GameAnalysisViewPortrait(
    navActions: List<LightAction>,
    otherActions: List<LightAction>
) {

    val context = LocalContext.current as MainActivity
    val gameAnalysisViewModel = context.gameAnalysisViewModel

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        SubHeader(
            modifier = Modifier.padding(4.dp, 16.dp),
            text = "${gameAnalysisViewModel.game.whitePlayer.name} vs ${gameAnalysisViewModel.game.blackPlayer.name}"
        )

        ChessBoardStatic(board = gameAnalysisViewModel.game.board)

        SubHeader(
            modifier = Modifier.padding(4.dp, 16.dp),
            text = "Moves"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (action in navActions) {
                LightActionView(action)
            }
        }

        WrappingRow {
            val lastMoveIndex = gameAnalysisViewModel.lastMoveIndex
            gameAnalysisViewModel.sans.forEachIndexed { index, san ->
                Text(
                    modifier = Modifier
                        .padding(8.dp, 4.dp)
                        .background(if (index == lastMoveIndex) Color.Green else Color.White)
                        .clickable(
                            onClick = {
                                gameAnalysisViewModel.jumpToMove(index)
                            }
                        ),
                    text = san
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (action in otherActions) {
                LightActionView(action)
            }
        }

    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun GameAnalysisViewLandscape(
    navActions: List<LightAction>,
    otherActions: List<LightAction>
) {

    val context = LocalContext.current as MainActivity
    val screenConfig = context.screenConfig
    val gameAnalysisViewModel = context.gameAnalysisViewModel


    Row(modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            ChessBoardStatic(
                modifier = Modifier.width((screenConfig.screenWidthDp - 64).dp),
                board = gameAnalysisViewModel.game.board
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            SubHeader(
                modifier = Modifier.padding(4.dp, 16.dp),
                text = "${gameAnalysisViewModel.game.whitePlayer.name} vs ${gameAnalysisViewModel.game.blackPlayer.name}"
            )
            SubHeader(
                modifier = Modifier.padding(4.dp, 16.dp),
                text = "Moves"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (action in navActions) {
                    LightActionView(action)
                }
            }
            WrappingRow {
                val lastMoveIndex = gameAnalysisViewModel.lastMoveIndex
                gameAnalysisViewModel.sans.forEachIndexed { index, san ->
                    Text(
                        modifier = Modifier
                            .padding(8.dp, 4.dp)
                            .background(if (index == lastMoveIndex) Color.Green else Color.White)
                            .clickable(
                                onClick = {
                                    gameAnalysisViewModel.jumpToMove(index)
                                }
                            ),
                        text = san
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (action in otherActions) {
                    LightActionView(action)
                }
            }

        }

    }

}