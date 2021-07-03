package com.nwagu.android.chessboy.screens.main.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.nwagu.android.chessboy.screens.analysis.view.GameAnalysisView
import com.nwagu.android.chessboy.screens.history.view.HistoryView
import com.nwagu.android.chessboy.screens.navigation.Dialog
import com.nwagu.android.chessboy.screens.navigation.Screen
import com.nwagu.android.chessboy.screens.newgame.view.EnableLocationPrompt
import com.nwagu.android.chessboy.screens.newgame.view.NewBluetoothGameView
import com.nwagu.android.chessboy.screens.newgame.view.NewGameView
import com.nwagu.android.chessboy.screens.play.view.PlayView
import com.nwagu.android.chessboy.screens.play.view.SelectPromotionPiecePrompt
import com.nwagu.android.chessboy.screens.settings.SettingsView
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun MainView() {

    val navHostController = rememberNavController()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )

    NavHost(navHostController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            BottomSheetWrapper(
                bottomSheetScaffoldState,
                { PlayView(navHostController) },
                { HomeView(bottomSheetScaffoldState, navHostController) }
            )
        }
        composable(Screen.NewGame.route) {
            BottomSheetWrapper(
                bottomSheetScaffoldState,
                { PlayView(navHostController) },
                { NewGameView(bottomSheetScaffoldState, navHostController) }
            )
        }
        composable(Screen.NewBluetoothGame.route) {
            BottomSheetWrapper(
                bottomSheetScaffoldState,
                { PlayView(navHostController) },
                { NewBluetoothGameView(bottomSheetScaffoldState, navHostController) }
            )
        }
        composable(Screen.GameAnalysis.route) {
            BottomSheetWrapper(
                bottomSheetScaffoldState,
                { PlayView(navHostController) },
                { GameAnalysisView(navHostController) }
            )
        }
        composable(Screen.History.route) {
            BottomSheetWrapper(
                bottomSheetScaffoldState,
                { PlayView(navHostController) },
                { HistoryView(navHostController) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsView(navHostController)
        }
        dialog(
            Dialog.SelectPromotionPiece.route + "/{pendingMove}",
            arguments = listOf(navArgument("pendingMove") { type = NavType.StringType })
        ) { backStackEntry ->
            SelectPromotionPiecePrompt(
                navHostController,
                backStackEntry.arguments!!.getString("pendingMove")!!
            )
        }
        dialog(Dialog.EnableLocation.route) {
            EnableLocationPrompt(navHostController)
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun BottomSheetWrapper(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    mainContent: @Composable BoxScope.() -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 16.dp,
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = Color.White.copy(alpha = 0f),
        sheetPeekHeight = 64.dp,
        sheetContent = sheetContent
    ) {

        Box(
            Modifier
                .fillMaxHeight()
                .padding(0.dp, 0.dp, 0.dp, bottom = 64.dp)
        ) {

            mainContent()

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