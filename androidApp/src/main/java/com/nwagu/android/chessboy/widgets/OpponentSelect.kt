package com.nwagu.android.chessboy.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nwagu.chessboy.sharedmodels.players.SelectablePlayer
import com.nwagu.chessboy.sharedmodels.players.UCIChessEngine
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColor
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColorLight
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun OpponentSelect(
    modifier: Modifier = Modifier,
    items: List<SelectablePlayer>,
    selectedItem: SelectablePlayer?,
    onSelect: (SelectablePlayer) -> Unit
) {

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column {
            repeat(items.size) { index ->
                val player = items[index]
                PlayerSelect(player, player == selectedItem, onSelect)
            }
        }
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun PlayerSelect(
    player: SelectablePlayer,
    isSelected: Boolean,
    onSelect: (SelectablePlayer) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onSelect(player)
        },
        shape = RoundedCornerShape(4.dp),
        backgroundColor = if (isSelected) colorResource(getPrimaryColorLight()) else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) colorResource(getPrimaryColor()) else Color.Transparent),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = player.name,
                )
            }

            // advanced settings space
            AnimatedVisibility(
                visible = isSelected,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box {
                    Column {
                        if (player is UCIChessEngine) {
                            var playerLevel by remember { mutableStateOf(player.level) }
                            Text(
                                text = "Level $playerLevel",
                                style = TextStyle(Color.Gray, fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic)
                            )
                            Slider(
                                modifier = Modifier.padding(8.dp, 0.dp),
                                valueRange = player.minLevel.toFloat()..player.maxLevel.toFloat(),
                                steps = (player.maxLevel - player.minLevel) - 1,
                                value = playerLevel.toFloat(),
                                enabled = true,
                                onValueChange = {
                                    player.level = it.roundToInt()
                                    playerLevel = it.roundToInt()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
