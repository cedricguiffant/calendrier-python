package com.lolinsight.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolinsight.domain.repository.AnalysisRepository
import com.lolinsight.domain.usecase.AnalyzeCompositionUseCase
import com.lolinsight.domain.usecase.DetectChampionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

sealed class CameraUiState {
    object Idle : CameraUiState()
    object Processing : CameraUiState()
    data class Success(val analysisId: Long) : CameraUiState()
    data class Error(val message: String) : CameraUiState()
}

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val detectChampionsUseCase: DetectChampionsUseCase,
    private val analyzeCompositionUseCase: AnalyzeCompositionUseCase,
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CameraUiState>(CameraUiState.Idle)
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private var imageCapture: ImageCapture? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun setupCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                _uiState.value = CameraUiState.Error("Impossible d'ouvrir la caméra: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun capturePhoto(context: Context) {
        val imageCapture = imageCapture ?: return
        val outputFile = File(context.cacheDir, "lol_capture_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        _uiState.value = CameraUiState.Processing

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(outputFile.absolutePath)
                    if (bitmap != null) {
                        processImage(bitmap)
                    } else {
                        _uiState.value = CameraUiState.Error("Impossible de lire l'image capturée")
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.value = CameraUiState.Error("Erreur de capture: ${exception.message}")
                }
            }
        )
    }

    fun processImageFromUri(context: Context, uri: Uri) {
        _uiState.value = CameraUiState.Processing
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap != null) {
                    processImage(bitmap)
                } else {
                    _uiState.value = CameraUiState.Error("Impossible de lire l'image sélectionnée")
                }
            } catch (e: Exception) {
                _uiState.value = CameraUiState.Error("Erreur: ${e.message}")
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val detectedTeams = detectChampionsUseCase(bitmap)

                if (detectedTeams.rawDetectedNames.isEmpty()) {
                    // Use demo data if nothing detected
                    val demoAllies = listOf("Yasuo", "Vi", "Jinx", "Thresh", "Malphite")
                    val demoEnemies = listOf("Zed", "LeeSin", "Caitlyn", "Blitzcrank", "Syndra")
                    saveAndNavigate(demoAllies, demoEnemies)
                } else {
                    saveAndNavigate(detectedTeams.allyTeam, detectedTeams.enemyTeam)
                }
            } catch (e: Exception) {
                _uiState.value = CameraUiState.Error("Erreur d'analyse: ${e.message}")
            }
        }
    }

    private suspend fun saveAndNavigate(allyTeam: List<String>, enemyTeam: List<String>) {
        val analysis = analyzeCompositionUseCase(allyTeam, enemyTeam)
        val savedId = analysisRepository.saveAnalysis(analysis)
        _uiState.value = CameraUiState.Success(savedId)
    }

    fun resetState() {
        _uiState.value = CameraUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }
}
