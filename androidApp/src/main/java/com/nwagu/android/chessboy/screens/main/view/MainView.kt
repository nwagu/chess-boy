package com.nwagu.android.chessboy.screens.main.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nwagu.android.chessboy.R
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.screens.history.view.HistoryView
import com.nwagu.android.chessboy.screens.settings.SettingsView
import com.nwagu.android.chessboy.screens.analysis.view.GameAnalysisView
import com.nwagu.android.chessboy.screens.model.Screen
import com.nwagu.android.chessboy.screens.newgame.view.NewBluetoothGameView
import com.nwagu.android.chessboy.screens.newgame.view.NewGameView
import com.nwagu.android.chessboy.screens.play.view.PlayView
import com.nwagu.android.chessboy.ui.data.QuickAction
import com.nwagu.android.chessboy.util.isBluetoothGame
import com.nwagu.android.chessboy.widgets.Header
import com.nwagu.android.chessboy.widgets.QuickActionView
import com.nwagu.android.chessboy.widgets.SimpleFlowRow
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MainView(dialogController: DialogController) {

    val navHostController = rememberNavController()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 16.dp,
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = Color.White.copy(alpha = 0f),
        sheetPeekHeight = 64.dp,
        sheetContent = {
            PlayView(navHostController, dialogController)
        }
    ) {

        Box(
            Modifier
                .fillMaxHeight()
                .padding(0.dp, 0.dp, 0.dp, bottom = 64.dp)
        ) {

            NavHost(navHostController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeView(bottomSheetScaffoldState, navHostController)
                }
                composable(Screen.NewGame.route) {
                    NewGameView(bottomSheetScaffoldState, navHostController, dialogController)
                }
                composable(Screen.NewBluetoothGame.route) {
                    NewBluetoothGameView(bottomSheetScaffoldState, navHostController, dialogController)
                }
                composable(Screen.GameAnalysis.route) {
                    GameAnalysisView(navHostController, dialogController)
                }
                composable(Screen.History.route) {
                    HistoryView(navHostController, dialogController)
                }
                composable(Screen.Settings.route) {
                    SettingsView(navHostController, dialogController)
                }
            }

            BackHandler(
                enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded,
                onBack = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            )
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun HomeView(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    navHostController: NavHostController
) {

    val context = LocalContext.current as MainActivity
    val playViewModel = context.playViewModel

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .padding(16.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.logo),
                modifier = Modifier
                    .width(100.dp),
                contentDescription = "Chess Boy Logo",
                contentScale = ContentScale.Fit
            )
        }

        Header(Modifier.padding(0.dp, 16.dp),"Play")

        val playActions = listOf(
            QuickAction("Continue current game", R.drawable.img_white_king) {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            },
            QuickAction("New game", R.drawable.img_white_king) {
                navHostController.navigate(Screen.NewGame.route)
            },
            QuickAction("New bluetooth game", R.drawable.img_white_king) {
                if (playViewModel.game.isBluetoothGame())
                    playViewModel.endCurrentGame()
                navHostController.navigate(Screen.NewBluetoothGame.route)
            }
        )

        SimpleFlowRow {
            repeat(playActions.size) {
                QuickActionView(playActions[it])
            }
        }

        Header(Modifier.padding(0.dp, 16.dp),"History")

        val historyActions = listOf(
            QuickAction("Most recent games", R.drawable.img_white_king) {
                navHostController.navigate(Screen.History.route)
            }
        )

        SimpleFlowRow {
            repeat(historyActions.size) {
                QuickActionView(historyActions[it])
            }
        }

    }
}

