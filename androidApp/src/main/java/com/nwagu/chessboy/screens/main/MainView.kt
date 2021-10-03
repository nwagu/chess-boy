package com.nwagu.chessboy.screens.main

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
import com.nwagu.chessboy.screens.analysis.ConfirmDeletePrompt
import com.nwagu.chessboy.screens.analysis.GameAnalysisView
import com.nwagu.chessboy.screens.history.HistoryView
import com.nwagu.chessboy.screens.navigation.Dialog
import com.nwagu.chessboy.screens.navigation.Screen
import com.nwagu.chessboy.screens.newgame.EnableLocationPrompt
import com.nwagu.chessboy.screens.newgame.NewBluetoothGameView
import com.nwagu.chessboy.screens.newgame.NewGameView
import com.nwagu.chessboy.screens.play.PlayView
import com.nwagu.chessboy.screens.play.SelectPromotionPiecePrompt
import com.nwagu.chessboy.screens.settings.SettingsView
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

    @Composable
    fun bottomSheetWrapperOf(mainContent: @Composable BoxScope.() -> Unit) {
        BottomSheetWrapper(
            bottomSheetScaffoldState,
            { PlayView(navHostController) },
            mainContent
        )
    }

    NavHost(navHostController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            bottomSheetWrapperOf { HomeView(bottomSheetScaffoldState, navHostController) }
        }
        composable(Screen.NewGame.route) {
            bottomSheetWrapperOf { NewGameView(bottomSheetScaffoldState, navHostController) }
        }
        composable(Screen.NewBluetoothGame.route) {
            bottomSheetWrapperOf { NewBluetoothGameView(bottomSheetScaffoldState, navHostController) }
        }
        composable(Screen.GameAnalysis.route) {
            bottomSheetWrapperOf { GameAnalysisView(navHostController) }
        }
        composable(Screen.History.route) {
            bottomSheetWrapperOf { HistoryView(navHostController) }
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
        dialog(
            Dialog.ConfirmDeleteGame.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            ConfirmDeletePrompt(navHostController, backStackEntry.arguments!!.getLong("id"))
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