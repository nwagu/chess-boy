package com.nwagu.android.chessboy.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
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
import com.nwagu.android.chessboy.R
import com.nwagu.android.chessboy.screens.navigation.Screen
import com.nwagu.android.chessboy.model.QuickAction
import com.nwagu.android.chessboy.widgets.Header
import com.nwagu.android.chessboy.widgets.QuickActionView
import com.nwagu.android.chessboy.widgets.WrappingRow
import com.nwagu.chessboy.sharedmodels.utils.isBluetoothGame
import kotlinx.coroutines.launch

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
            QuickAction("Continue current game") {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            },
            QuickAction("New game with computer") {
                navHostController.navigate(Screen.NewGame.route)
            },
            QuickAction("New bluetooth game") {
                if (playViewModel.game.isBluetoothGame())
                    playViewModel.endCurrentGame()
                navHostController.navigate(Screen.NewBluetoothGame.route)
            }
        )

        WrappingRow {
            repeat(playActions.size) {
                QuickActionView(playActions[it])
            }
        }

        Header(Modifier.padding(0.dp, 16.dp),"History")

        val historyActions = listOf(
            QuickAction("Your recent games") {
                navHostController.navigate(Screen.History.route)
            }
        )

        WrappingRow {
            repeat(historyActions.size) {
                QuickActionView(historyActions[it])
            }
        }

    }
}

