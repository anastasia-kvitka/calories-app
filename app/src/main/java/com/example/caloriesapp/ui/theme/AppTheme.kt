package com.example.caloriesapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// monochrome colors
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Gray = Color(0xFFCCCCCC)

private val MonoLight = lightColorScheme(
    primary = Black,
    onPrimary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    outline = Black,
    secondary = Black,
    onSecondary = White,
)

@Composable
fun MonoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MonoLight,
        typography = MaterialTheme.typography,
        content = content
    )
}
