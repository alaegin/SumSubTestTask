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

    private val controller = LifecycleCameraController(context)
    private val analyzerExecutor = Dispatchers.Default.asExecutor()

    suspend fun startCamera() {
        previewView.controller = controller
        setCameraFacing(isFront = true)
        controller.bindToLifecycle(lifecycleOwner)
    }

    suspend fun setImageAnalyzer(analyzer: ImageAnalysis.Analyzer) {
        awaitCamera()
        controller.imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        controller.setImageAnalysisAnalyzer(analyzerExecutor, analyzer)
    }

    fun release() {
        previewView.controller = null
        controller.unbind()
    }

    suspend fun setCameraFacing(isFront: Boolean) {
        awaitCamera()
        controller.cameraSelector = if (isFront) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    suspend fun getCapabilities(): Capabilities {
        awaitCamera()
        return Capabilities(
            hasFrontCamera = controller.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA),
            hasBackCamera = controller.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA),
        )
    }

    private suspend fun awaitCamera() {
        controller.initializationFuture.await()
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
