package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nwagu.chess.Player

interface SelectableOpponent: Player

@Composable
fun OpponentSelect(
    modifier: Modifier = Modifier,
    items: List<SelectableOpponent>,
    selectedItem: SelectableOpponent?,
    onSelect: (SelectableOpponent) -> Unit
) {

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(4.dp),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column {

            repeat(items.size) { index ->

                Row(modifier = Modifier
                    .padding(4.dp)
                    .height(60.dp)
                    .clickable(onClick = {
                        onSelect(items[index])
                    }),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val isSelected = items[index] == selectedItem

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(text = items[index].name)

                        /*if (isSelected && items[index] is AI) {
                            Slider(
                                valueRange = 1f..10f,
                                value = (items[index] as AI).level.toFloat(),
                                onValueChange = { (items[index] as AI).level = it.toInt() }
                            )
                        }*/
                    }

                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            onSelect(items[index])
                        }
                    )
                }
            }
        }
    }

}
