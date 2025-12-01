package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.ui.theme.White
import com.example.caloriesapp.ui.components.MonoSelectableCard

@Composable
fun GenderSelectionScreen(
    initialGender: String?,          // "male", "female", "other", or null
    onContinue: (String?) -> Unit     // we pass selected gender forward
) {
    var selected by remember { mutableStateOf(initialGender) }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MonoFilledButton(
                    text = "Continue",
                    onClick = { onContinue(selected) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(32.dp))

            Text(
                "Select gender",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Choose the option you identify with.\nThis helps improve macro recommendations.",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            // --- OPTIONS ---
            MonoSelectableCard(
                label = "Male",
                selected = selected == "male",
                onClick = { selected = "male" }
            )

            Spacer(Modifier.height(12.dp))

            MonoSelectableCard(
                label = "Female",
                selected = selected == "female",
                onClick = { selected = "female" }
            )

            Spacer(Modifier.height(12.dp))

            MonoSelectableCard(
                label = "Other",
                selected = selected == "other",
                onClick = { selected = "other" }
            )
        }
    }
}
