package com.nwagu.android.chessboy.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.widgets.ScreenTopBar

@ExperimentalMaterialApi
@Composable
fun SettingsView(
    navHostController: NavHostController
) {

    Column(modifier = Modifier
        .background(color = Color.White)
        .fillMaxSize()
        .padding(16.dp, 8.dp)
    ) {

        ScreenTopBar(title = "Settings", navHostController)

        Column(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

        }

    }

}