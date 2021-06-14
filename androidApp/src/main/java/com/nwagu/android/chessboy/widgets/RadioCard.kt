package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nwagu.android.chessboy.ui.AppColor

@ExperimentalMaterialApi
@Composable
fun RadioCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: (() -> Unit) = {},
    text: String
) {

    Card(
        modifier = modifier
            .padding(0.dp, 0.dp, 8.dp, 8.dp)
            .width(100.dp)
            .padding(8.dp)
            .aspectRatio(1f),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        backgroundColor = if (isSelected) AppColor.PrimaryLight else Color.White,
        elevation = 2.dp
    ) {
        Box {
            RadioButton(
                modifier = Modifier.align(Alignment.TopEnd),
                selected = isSelected,
                enabled = enabled,
                onClick = {
                    onClick()
                }
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
            )
        }
    }

}