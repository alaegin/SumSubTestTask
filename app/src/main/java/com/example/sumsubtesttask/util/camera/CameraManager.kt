package com.example.sumsubtesttask.util.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.guava.await

class CameraManager @AssistedInject constructor(
    @Assisted
    private val previewView: PreviewView,
    @Assisted
    private val lifecycleOwner: LifecycleOwner,
    @ApplicationContext
    private val context: Context,
) {

    private val cameraController = LifecycleCameraController(context)
    private val analyzerExecutor = Dispatchers.Default.asExecutor()
    private var cameraStarted = false

    suspend fun startCamera() = awaitCamera {
        if (cameraStarted) {
            return
        }

        val capabilities = getCapabilities()
        setCameraFacing(isFront = capabilities.hasFrontCamera)

        previewView.controller = cameraController
        cameraController.bindToLifecycle(lifecycleOwner)

        cameraStarted = true
    }

    suspend fun setImageAnalyzer(analyzer: ImageAnalysis.Analyzer) = awaitCamera {
        cameraController.imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        cameraController.setImageAnalysisAnalyzer(analyzerExecutor, analyzer)
    }

    fun release() {
        previewView.controller = null
        cameraController.unbind()
    }

    suspend fun setCameraFacing(isFront: Boolean) = awaitCamera {
        cameraController.cameraSelector = if (isFront) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    suspend fun getCapabilities(): Capabilities = awaitCamera {
        return Capabilities(
            hasFrontCamera = cameraController.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA),
            hasBackCamera = cameraController.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA),
        )
    }

    private suspend inline fun <T> awaitCamera(block: () -> T): T {
        if (!cameraController.initializationFuture.isDone) {
            cameraController.initializationFuture.await()
        }

        return block()
    }

    @AssistedFactory
    interface Factory {
        fun create(previewView: PreviewView, lifecycleOwner: LifecycleOwner): CameraManager
    }

    data class Capabilities(
        val hasFrontCamera: Boolean,
        val hasBackCamera: Boolean,
    )
}
