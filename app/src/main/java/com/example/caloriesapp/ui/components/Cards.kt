package com.example.caloriesapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.ui.theme.White

@Composable
fun MonoCard(content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        border = BorderStroke(1.dp, Black),
        shape = RoundedCornerShape(16.dp)
    ) {
        content()
    }
}

@Composable
fun MonoSelectableCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .noRippleClickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Black else White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 6.dp else 2.dp
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                style = MonoTypography.bodyLarge.copy(
                    color = if (selected) White else Black
                )
            )
        }
    }
}