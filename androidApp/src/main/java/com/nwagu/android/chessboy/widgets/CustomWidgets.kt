package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nwagu.android.chessboy.model.data.LightAction
import com.nwagu.android.chessboy.model.data.QuickAction
import com.nwagu.android.chessboy.ui.AppColor

@Composable
fun Header(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 20.sp,
        style = TextStyle(AppColor.Primary, fontWeight = FontWeight.Bold)
    )
}

@Composable
fun SubHeader(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        style = TextStyle(Color.Black, fontWeight = FontWeight.Bold)
    )
}

@Composable
fun QuickActionView(
    quickAction: QuickAction
) {
    Card(
        Modifier
            .padding(0.dp, 0.dp, 8.dp, 8.dp)
            .width(120.dp)
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable(
                onClick = {
                    quickAction.action()
                }
            ),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        elevation = 2.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = quickAction.displayName)
        }
    }
}

@Composable
fun LightActionView(lightAction: LightAction) {
    Button(modifier = Modifier
        .padding(8.dp),
        onClick = {
            lightAction.action()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.activityBackground),
        content = {
            Text(
                modifier = Modifier,
                text = lightAction.displayName,
                color = Color.Black
            )
        }
    )
}

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.Primary)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            color = Color.White
        )
    }
}