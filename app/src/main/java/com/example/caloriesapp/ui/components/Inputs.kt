package com.example.caloriesapp.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White

@Composable
fun MonoInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Black) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedIndicatorColor = Black,
            unfocusedIndicatorColor = Black,
            cursorColor = Black,
            focusedTextColor = Black,
            unfocusedTextColor = Black
        )
    )
}