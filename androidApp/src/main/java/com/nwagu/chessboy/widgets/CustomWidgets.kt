package com.nwagu.chessboy.widgets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nwagu.chessboy.model.ViewAction
import com.nwagu.chess.representation.PGN_HEADER_BLACK_PLAYER
import com.nwagu.chess.representation.PGN_HEADER_WHITE_PLAYER
import com.nwagu.chess.representation.getHeaderValueFromPgn
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColor
import com.nwagu.chessboy.sharedmodels.resources.getScreenBackgroundColor

@Composable
fun Header(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 20.sp,
        style = TextStyle(colorResource(getPrimaryColor()), fontWeight = FontWeight.Bold)
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

@ExperimentalMaterialApi
@Composable
fun QuickActionView(
    viewAction: ViewAction
) {
    Card(
        modifier = Modifier
            .padding(0.dp, 0.dp, 8.dp, 8.dp)
            .width(120.dp)
            .padding(8.dp)
            .aspectRatio(1f),
        onClick = {
            viewAction.action()
        },
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = 0.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = viewAction.displayName)
        }
    }
}

@Composable
fun LightActionView(viewAction: ViewAction) {
    Button(modifier = Modifier
        .padding(8.dp),
        onClick = {
            viewAction.action()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(getScreenBackgroundColor())),
        content = {
            Text(
                modifier = Modifier,
                text = viewAction.displayName,
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
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(getPrimaryColor()))
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            color = Color.White
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun PreviousGameView(
    modifier: Modifier = Modifier,
    pgn: String,
    onClick: () -> Unit
) {

    val whitePlayer = getHeaderValueFromPgn(PGN_HEADER_WHITE_PLAYER, pgn)
    val blackPlayer = getHeaderValueFromPgn(PGN_HEADER_BLACK_PLAYER, pgn)

    Card(
        modifier = modifier.padding(8.dp),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White,
        elevation = 1.dp
    ) {

        Row(
            modifier = Modifier.height(60.dp).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$whitePlayer(W) vs $blackPlayer(B)",
                style = TextStyle(Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Normal)
            )
        }
    }
}