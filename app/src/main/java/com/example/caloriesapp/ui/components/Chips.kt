package com.example.caloriesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.ui.theme.White
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults

@Composable
fun MonoFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        label = {
            Text(label,
                style = MonoTypography.bodyLarge.copy(
                    color = if (selected) White else Black
                )
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier
            .noRippleClickable { onClick() },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Black.copy(alpha = 0.06f),
            selectedContainerColor = Black
        )
    )
}

@Composable
fun MonoDayChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontSize = 14.sp,
        color = if (selected) White else Black,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Black else White)
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .clickable { onClick() }
    )
}