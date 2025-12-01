package com.example.caloriesapp.ui.theme

import androidx.compose.material3.Typography

/**
 * Minimalistic black-and-white typography for your Mono UI Kit.
 * All text styles use pure Black for content, unless overridden in components.
 */

val MonoTypography = Typography().run {
    Typography(
        displayLarge = displayLarge.copy(color = Black),
        displayMedium = displayMedium.copy(color = Black),
        displaySmall = displaySmall.copy(color = Black),

        headlineLarge = headlineLarge.copy(color = Black),
        headlineMedium = headlineMedium.copy(color = Black),
        headlineSmall = headlineSmall.copy(color = Black),

        titleLarge = titleLarge.copy(color = Black),
        titleMedium = titleMedium.copy(color = Black),
        titleSmall = titleSmall.copy(color = Black),

        bodyLarge = bodyLarge.copy(color = Black),
        bodyMedium = bodyMedium.copy(color = Black),
        bodySmall = bodySmall.copy(color = Black),

        labelLarge = labelLarge.copy(color = Black),
        labelMedium = labelMedium.copy(color = Black),
        labelSmall = labelSmall.copy(color = Black),
    )
}

