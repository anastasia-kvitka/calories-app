package com.example.caloriesapp.ui.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White

@Composable
fun MonoSnackbar(text: String) {
    Snackbar(
        containerColor = White,
        contentColor = Black,
        actionContentColor = Black
    ) {
        Text(text)
    }
}