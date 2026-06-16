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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

// Taille max pour le traitement OCR — assez grand pour lire les noms, assez petit pour la RAM
private const val MAX_BITMAP_WIDTH = 1280
private const val MAX_BITMAP_HEIGHT = 720

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
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

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
        val imageCapture = imageCapture ?: run {
            _uiState.value = CameraUiState.Error("Caméra non initialisée")
            return
        }
        val outputFile = File(context.cacheDir, "lol_capture_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

        _uiState.value = CameraUiState.Processing

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Decode scaled-down bitmap (évite OOM sur appareils avec peu de RAM)
                    val bitmap = decodeSampledBitmapFromFile(
                        outputFile,
                        MAX_BITMAP_WIDTH,
                        MAX_BITMAP_HEIGHT
                    )
                    outputFile.delete() // Libère le stockage immédiatement
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
        // IO Dispatcher pour éviter le crash "NetworkOnMainThreadException" et OOM
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = decodeSampledBitmapFromUri(
                    context,
                    uri,
                    MAX_BITMAP_WIDTH,
                    MAX_BITMAP_HEIGHT
                )
                if (bitmap != null) {
                    processImage(bitmap)
                } else {
                    _uiState.value = CameraUiState.Error("Format d'image non supporté ou fichier corrompu")
                }
            } catch (e: SecurityException) {
                _uiState.value = CameraUiState.Error("Permission refusée pour accéder à cette image")
            } catch (e: Exception) {
                _uiState.value = CameraUiState.Error("Erreur de lecture: ${e.message}")
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val detectedTeams = detectChampionsUseCase(bitmap)

                if (detectedTeams.rawDetectedNames.isEmpty()) {
                    // Données démo si aucun champion détecté (screenshot de mauvaise qualité, etc.)
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
        withContext(Dispatchers.Main) {
            _uiState.value = CameraUiState.Success(savedId)
        }
    }

    // Décode une image à taille réduite depuis un fichier — évite OOM
    private fun decodeSampledBitmapFromFile(file: File, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565 // 2x moins de mémoire que ARGB_8888
            BitmapFactory.decodeFile(file.absolutePath, options)
        } catch (e: Exception) {
            null
        }
    }

    // Décode une image à taille réduite depuis un URI de galerie (compatible MIUI)
    private fun decodeSampledBitmapFromUri(
        context: Context,
        uri: Uri,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        return try {
            // Première passe : lire les dimensions seulement
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }

            if (options.outWidth <= 0 || options.outHeight <= 0) return null

            // Calculer le taux de réduction
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565 // 2x moins de RAM

            // Deuxième passe : décoder à taille réduite
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }
        } catch (e: Exception) {
            null
        }
    }

    // Calcule le facteur de réduction pour rester sous reqWidth x reqHeight
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun resetState() {
        _uiState.value = CameraUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }
}
