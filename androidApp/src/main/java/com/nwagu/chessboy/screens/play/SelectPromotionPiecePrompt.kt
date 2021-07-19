package com.nwagu.chessboy.screens.play

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nwagu.chessboy.R
import com.nwagu.chessboy.util.Utils
import com.nwagu.chess.model.ChessPieceType
import com.nwagu.chess.model.Promotion
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun SelectPromotionPiecePrompt(
    navHostController: NavHostController,
    pendingMoveStr: String
) {

    val context = Utils.unwrap(LocalContext.current)
    val playViewModel = context.playViewModel

    fun setPromotionPieceAndMakeMove(promotionPieceType: ChessPieceType) {
        val pendingMove = Json.decodeFromString<Promotion>(pendingMoveStr)
        pendingMove.promotionType = promotionPieceType
        playViewModel.makeUserMove(pendingMove)
    }

    Card(modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(2.dp),
        backgroundColor = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        elevation = 0.dp
    ) {
        Column {

            Text(
                modifier = Modifier.padding(8.dp),
                text = "Promote to:",
                style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(onClick = {
                            setPromotionPieceAndMakeMove(ChessPieceType.QUEEN)
                            navHostController.navigateUp()
                        }),
                    painter = painterResource(R.drawable.img_white_queen),
                    contentDescription = "white_queen"
                )
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(onClick = {
                            setPromotionPieceAndMakeMove(ChessPieceType.KNIGHT)
                            navHostController.navigateUp()
                        }),
                    painter = painterResource(R.drawable.img_white_knight),
                    contentDescription = "white_knight"
                )
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(onClick = {
                            setPromotionPieceAndMakeMove(ChessPieceType.BISHOP)
                            navHostController.navigateUp()
                        }),
                    painter = painterResource(R.drawable.img_white_bishop),
                    contentDescription = "white_bishop"
                )
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable(onClick = {
                            setPromotionPieceAndMakeMove(ChessPieceType.ROOK)
                            navHostController.navigateUp()
                        }),
                    painter = painterResource(R.drawable.img_white_rook),
                    contentDescription = "white_rook"
                )
            }
        }
    }
}