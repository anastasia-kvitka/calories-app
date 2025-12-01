package com.example.caloriesapp.ui.screens

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.*
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.ui.components.MonoIconButton
import com.example.caloriesapp.R
import com.example.caloriesapp.ui.components.MonoCaptureButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onClose: () -> Unit,
    onImageCaptured: (File) -> Unit
) {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    if (cameraPermission.status.isGranted) {
        CameraScreenContent(onClose, onImageCaptured)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Camera permission required",
                style = MonoTypography.bodyLarge,
                color = Black
            )
        }
    }
}

@Composable
private fun CameraScreenContent(
    onClose: () -> Unit,
    onImageCaptured: (File) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    // Start Camera
    LaunchedEffect(Unit) {
        startCamera(context, lifecycleOwner, previewView, imageCapture)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Camera Preview
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Top Close Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            MonoIconButton(
                icon = R.drawable.ic_back,
                onClick = onClose
            )
        }

        // Bottom Capture Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            MonoCaptureButton {
                takePhoto(context, imageCapture, onImageCaptured)
            }
        }
    }
}

private fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    imageCapture: ImageCapture
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }, ContextCompat.getMainExecutor(context))
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (File) -> Unit
) {
    val photoFile = File(
        context.externalCacheDir,
        "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                exc.printStackTrace()
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onImageCaptured(photoFile)
            }
        }
    )
}
