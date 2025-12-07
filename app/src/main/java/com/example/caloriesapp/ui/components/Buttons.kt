package com.example.caloriesapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

@Composable
fun MonoFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        )
    ) {
        Text(text)
    }
}

@Composable
fun MonoOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Black.copy(alpha = 0.06f),
            contentColor = Black
        )
    ) {
        Text(text)
    }
}

/**
 * A reusable monochrome icon button (pure black/white).
 *
 * Used for navigation (back), camera actions, small actions.
 */
@Composable
fun MonoIconButton(
    icon: Int,
    modifier: Modifier = Modifier,
    size: Int = 44,                 // icon button size
    iconSize: Int = 20,            // inner icon size
    backgroundColor: androidx.compose.ui.graphics.Color = White,
    tint: androidx.compose.ui.graphics.Color = Black,
    cornerRadius: Int = size,      // size-based radius → full circle by default
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Composable
fun MonoCaptureButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp),
        contentAlignment = Alignment.Center
    ) {

        // Outer white ring
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(White, CircleShape)
        )

        // Inner black circle
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Black, CircleShape)
        )

        // Transparent clickable overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(White.copy(alpha = 0.001f))
                .noRippleClickable { onClick() }
        )
    }
}

