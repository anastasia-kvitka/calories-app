package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoOutlinedButton
import com.example.caloriesapp.ui.theme.MonoTypography

@Composable
fun HeightSelectionScreen(
    initialHeight: Int = 170,
    onSave: (Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var height by remember { mutableStateOf(initialHeight) }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MonoOutlinedButton(
                    text = "Back",
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                MonoFilledButton(
                    text = "Continue",
                    onClick = {
                        onSave(height)
                        onContinue()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(40.dp))

            Text(
                text = "Your Height",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Adjust your height (cm)",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = "$height cm",
                style = MonoTypography.displayLarge
            )

            Spacer(Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                MonoOutlinedButton(
                    text = "-",
                    onClick = { if (height > 120) height-- },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                MonoOutlinedButton(
                    text = "+",
                    onClick = { if (height < 230) height++ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
