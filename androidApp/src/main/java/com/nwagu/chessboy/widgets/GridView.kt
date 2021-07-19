package com.nwagu.chessboy.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> GridView(
    modifier: Modifier = Modifier,
    numberOfColumns: Int = 1,
    items: List<T>,
    itemView: @Composable (dataModal: T) -> Unit
) {
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        val rows = (items.size / numberOfColumns) + (if (items.size % numberOfColumns > 0) 1 else 0)
        
        repeat(rows) { row ->
            Row {
                repeat (numberOfColumns) { column ->
                    val i = (row * numberOfColumns) + column
                    if (i < items.size) {
                        Box(modifier = Modifier.weight(1f / numberOfColumns).fillMaxWidth(1f / numberOfColumns)) {
                            itemView(items[i])
                        }
                    }
                }
            }
        }
    }
}