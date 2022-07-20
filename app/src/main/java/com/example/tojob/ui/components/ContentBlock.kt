package com.example.tojob.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ContentBlock(
    modifier: Modifier = Modifier,
    minWidth: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
    horizontalPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    Surface(
        elevation = 2.dp,
        modifier = modifier
            .padding(horizontal = horizontalPadding)
            .defaultMinSize(minHeight = minHeight, minWidth = minWidth)
            .fillMaxWidth(),
        content = content,
        shape = RoundedCornerShape(4.dp)
    )
}
