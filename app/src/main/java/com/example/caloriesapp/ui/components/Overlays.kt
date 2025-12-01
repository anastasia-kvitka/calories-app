package com.example.caloriesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.theme.Black

@Composable
fun MonoCameraFrame() {
    Box(
        modifier = Modifier
            .padding(60.dp)
            .fillMaxSize()
    ) {
        Corner(Modifier.align(Alignment.TopStart))
        Corner(Modifier.align(Alignment.TopEnd))
        Corner(Modifier.align(Alignment.BottomStart))
        Corner(Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
fun Corner(mod: Modifier) {
    Box(mod.size(60.dp)) {
        Box(
            Modifier
                .height(4.dp)
                .width(34.dp)
                .background(Black)
        )
        Box(
            Modifier
                .width(4.dp)
                .height(34.dp)
                .background(Black)
        )
    }
}