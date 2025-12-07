package com.example.caloriesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.caloriesapp.ui.theme.White

@Composable
fun MonoScreenWithBottomBar(
    bottomBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(White)
        ) {
            content()
        }

        bottomBar()
    }
}
