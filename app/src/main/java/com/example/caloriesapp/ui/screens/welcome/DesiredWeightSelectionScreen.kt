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
import androidx.compose.runtime.collectAsState
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
import com.example.caloriesapp.viewmodel.OnboardingViewModel

@Composable
fun DesiredWeightSelectionScreen(
    viewModel: OnboardingViewModel,
    onSave: (Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var desiredWeight by remember { mutableStateOf(state.desiredWeight ?: 60) }

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
                        onSave(desiredWeight)
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
                text = "Target Weight",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Adjust your target weight (kg)",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = "$desiredWeight kg",
                style = MonoTypography.displayLarge
            )

            Spacer(Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                MonoOutlinedButton(
                    text = "-",
                    onClick = { if (desiredWeight > 30) desiredWeight-- },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                MonoOutlinedButton(
                    text = "+",
                    onClick = { if (desiredWeight < 200) desiredWeight++ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
