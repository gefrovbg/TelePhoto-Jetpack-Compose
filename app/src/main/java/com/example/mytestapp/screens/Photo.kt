package com.example.mytestapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mytestapp.MainActivity.Companion.rotation
import com.example.mytestapp.ui.theme.MyTestAppTheme
import com.example.telephoto.data.storage.filesystem.usecase.GetOutputDirectoryUseCase
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


private val getOutputDirectory = GetOutputDirectoryUseCase()
private val imageCapture: ImageCapture = ImageCapture.Builder().build()
@SuppressLint("StaticFieldLeak")
private lateinit var context: Context

@Composable
fun PhotoScreen() {

    val booleanPermission = remember { mutableStateOf(false) }
    val lensFacing = CameraSelector.LENS_FACING_BACK
    context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val preview = androidx.camera.core.Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { imageCapture }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        rotation.observe(lifecycleOwner){
            imageCapture.targetRotation = it
        }
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    MyTestAppTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(colors.onSecondary)) {

            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                booleanPermission.value = isGranted
                if (!isGranted){
                    Manifest.permission.CAMERA
                }
            }

            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) -> {
                    booleanPermission.value = true
                }
                else -> {
                    booleanPermission.value = false
                }
            }

            if (booleanPermission.value) {
                AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
            }else{
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .padding(20.dp),
                        style = typography.h4,
                        textAlign = TextAlign.Center,
                        text = "Permission is required to use the camera!"
                    )

                    Button(
                        shape = RoundedCornerShape(20.dp),
                        onClick = {
                            launcher.launch(Manifest.permission.CAMERA)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colors.onSecondary)
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp, 8.dp),
                            color = colors.primary,
                            text = "ALLOW"
                        )
                    }
                }
            }
        }
    }
}

fun takePhoto(successCallback: (File) -> Unit, errorCallback: (String) -> Unit) {
    val outputDirectory  = getOutputDirectory.execute(context)
    val photoFile = File(outputDirectory, "photo.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                errorCallback("Photo capture failed: ${exc.message}")
                Log.e(
                    "Bot",
                    "Photo capture failed: ${exc.message}",
                    exc
                )
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                successCallback(photoFile)
            }
        }
    )
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

@Composable
@Preview
fun PhotoScreenPreview() {
    PhotoScreen()
}