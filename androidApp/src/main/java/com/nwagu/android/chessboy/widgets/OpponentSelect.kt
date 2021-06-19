package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.players.GUIPlayer
import com.nwagu.android.chessboy.players.UCIChessEngine
import com.nwagu.android.chessboy.ui.AppColor
import com.nwagu.chess.Player
import kotlin.math.roundToInt

abstract class SelectablePlayer: GUIPlayer()

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
        backgroundColor = if (isSelected) AppColor.PrimaryLight else Color.Transparent,
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
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        onSelect(player)
                    }
                )

            }

            // advanced settings space
            if (isSelected) {
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
