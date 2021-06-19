package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.android.chessboy.ui.AppColor

@ExperimentalMaterialApi
@Composable
fun ScreenTopBar(
    title: String,
    navHostController: NavHostController,
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                navHostController.navigateUp()
            }
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Close", tint = Color.Black)
        }

        Header(Modifier.padding(0.dp, 8.dp), text = title)
    }

}