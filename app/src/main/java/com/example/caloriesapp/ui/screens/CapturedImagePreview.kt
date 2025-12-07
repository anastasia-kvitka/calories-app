package com.example.caloriesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import java.io.File
import com.example.caloriesapp.ui.theme.*
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoOutlinedButton

@Composable
fun CapturedImagePreview(
    file: File,
    onRetake: () -> Unit,
    onFinish: () -> Unit,
    onAnalyze: suspend (File) -> String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val fileUri = remember(file) {
        FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file
        )
    }

    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        // Background image (full screen)
        Image(
            painter = rememberAsyncImagePainter(fileUri),
            contentDescription = "Captured Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // BOTTOM OVERLAY
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(White.copy(alpha = 0.85f))
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ANALYSIS RESULT
            result?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(16.dp),
                        style = MonoTypography.bodyLarge,
                        color = Black
                    )
                }

                MonoFilledButton(
                    text = "Continue",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onFinish
                )

                Spacer(Modifier.height(12.dp))
            }

            // Loader
            if (loading) {
                CircularProgressIndicator(color = Black)
                Spacer(Modifier.height(16.dp))
            }

            // Analyze button (only when no result yet)
            if (!loading && result == null) {
                MonoFilledButton(
                    text = "Analyze Food",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        loading = true
                        result = null

                        scope.launch {
                            try {
                                result = onAnalyze(file)
                            } catch (e: Exception) {
                                result = "Error: ${e.localizedMessage}"
                            } finally {
                                loading = false
                            }
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))
            }

            MonoOutlinedButton(
                text = "Retake",
                modifier = Modifier.fillMaxWidth(),
                onClick = onRetake
            )
        }
    }
}
