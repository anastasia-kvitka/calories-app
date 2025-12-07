package com.example.caloriesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.theme.Black

@Composable
fun MonoDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Black)
}

@Composable
fun MonoFadedDivider() {
    HorizontalDivider(
        color = Black.copy(alpha = 0.4f),
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
}

