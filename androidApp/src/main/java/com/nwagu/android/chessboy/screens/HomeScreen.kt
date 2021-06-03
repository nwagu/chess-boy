package com.nwagu.android.chessboy.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nwagu.android.chessboy.R
import com.nwagu.android.chessboy.dialogs.DialogController
import com.nwagu.android.chessboy.model.data.QuickAction
import com.nwagu.android.chessboy.model.data.ScreenConfig
import com.nwagu.android.chessboy.vm.GameViewModel
import com.nwagu.android.chessboy.vm.NewBluetoothGameViewModel
import com.nwagu.android.chessboy.vm.NewGameViewModel
import com.nwagu.android.chessboy.widgets.ChessBoardThumbView
import com.nwagu.android.chessboy.widgets.Header
import com.nwagu.android.chessboy.widgets.QuickActionView
import com.nwagu.android.chessboy.widgets.SimpleFlowRow
import com.nwagu.chess.Game
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun HomeView(
    gameViewModel: GameViewModel,
    newGameViewModel: NewGameViewModel,
    newBluetoothGameViewModel: NewBluetoothGameViewModel,
    screenConfig: ScreenConfig,
    dialogController: DialogController
) {

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
            GameView(
                gameViewModel,
                screenConfig,
                navHostController,
                dialogController)
        }
    ) {

        Box(
            Modifier
                .fillMaxHeight()
                .padding(0.dp, 0.dp, 0.dp, bottom = 64.dp)
        ) {

            NavHost(navHostController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {

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

                        Header(Modifier.padding(0.dp, 16.dp),"Quick Actions")

                        val quickActions = listOf(
                            QuickAction("New bluetooth game", R.drawable.img_white_king) {
                                navHostController.navigate(Screen.NewBluetoothGame.route)
                            },
                            QuickAction("New game", R.drawable.img_white_king) {
                                navHostController.navigate(Screen.NewGame.route)
                            },
                            QuickAction("Continue current game", R.drawable.img_white_king) {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }
                        )

                        SimpleFlowRow {
                            repeat(quickActions.size) {
                                QuickActionView(quickActions[it])
                            }
                        }

                        Header(Modifier.padding(0.dp, 16.dp), "Your recent games")

                        val gameHistory = listOf<Game>()

                        SimpleFlowRow {
                            for(game in gameHistory) {
                                ChessBoardThumbView(modifier = Modifier.padding(4.dp), game)
                            }
                        }

                    }
                }
                composable(Screen.NewGame.route) {
                    NewGameView(
                        gameViewModel, newGameViewModel, screenConfig, bottomSheetScaffoldState, navHostController, dialogController
                    )
                }
                composable(Screen.NewBluetoothGame.route) {
                    NewBluetoothGameView(
                        gameViewModel, newBluetoothGameViewModel, screenConfig, bottomSheetScaffoldState, navHostController, dialogController
                    )
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

