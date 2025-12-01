package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.R
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoDivider
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.ui.theme.White

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(28.dp))

        // ——————————————————————
        // PHONE MOCKUP AREA
        // ——————————————————————
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .aspectRatio(0.55f)
                .clip(RoundedCornerShape(40.dp))
                .background(White)
        ) {
            Image(
                painter = painterResource(R.drawable.sample_food),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dynamic Island
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
                    .width(120.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Black.copy(alpha = 0.08f))
            )

            // Bottom white panel
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(White)
                    .padding(20.dp)
            ) {

                Text(
                    text = "Identified as",
                    style = MonoTypography.labelLarge
                )

                Text(
                    text = "Salad",
                    style = MonoTypography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Calories and macros will be recalculated automatically if weight is changed",
                    style = MonoTypography.bodySmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Weight: 325 g", style = MonoTypography.bodyMedium)
                    Text("Calories: 352", style = MonoTypography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(12.dp))

                MonoDivider()

                Spacer(modifier = Modifier.height(12.dp))

                Text("16.1 g protein", style = MonoTypography.bodyMedium)
                Text("14.6 g fat", style = MonoTypography.bodyMedium)
                Text("42.3 g carbs", style = MonoTypography.bodyMedium)

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ——————————————————————
        // TITLE AREA
        // ——————————————————————
        Text(
            text = "Track. Transform. Impress.",
            style = MonoTypography.displaySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Quickly track calories, get personalized tips,\nand transform your body for the better.",
            style = MonoTypography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        MonoFilledButton(
            text = "Continue",
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth(0.9f)
        )
    }
}

