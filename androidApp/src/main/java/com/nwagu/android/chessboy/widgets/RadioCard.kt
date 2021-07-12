package com.nwagu.android.chessboy.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColor
import com.nwagu.chessboy.sharedmodels.resources.getPrimaryColorLight

@ExperimentalMaterialApi
@Composable
fun RadioCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
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
        backgroundColor = if (isSelected) colorResource(getPrimaryColorLight()) else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) colorResource(getPrimaryColor()) else Color.LightGray),
        elevation = 0.dp
    ) {
        Box {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
            )
        }
    }

}