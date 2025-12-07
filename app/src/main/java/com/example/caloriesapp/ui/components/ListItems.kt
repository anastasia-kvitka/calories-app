package com.example.caloriesapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.theme.Black

@Composable
fun MonoListItem(title: String, subtitle: String? = null) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(title, color = Black, fontWeight = FontWeight.Bold)
        subtitle?.let {
            Text(it, color = Black.copy(alpha = 0.7f))
        }
        HorizontalDivider(color = Black, thickness = 1.dp)
    }
}