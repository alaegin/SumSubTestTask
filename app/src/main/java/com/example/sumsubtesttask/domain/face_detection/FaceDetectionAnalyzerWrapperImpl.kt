package com.example.sumsubtesttask.domain.face_detection

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import com.example.sumsubtesttask.domain.face_detection.model.DetectedFace
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import javax.inject.Inject

class FaceDetectionAnalyzerWrapperImpl @Inject constructor() : FaceDetectionAnalyzerWrapper {

    override val detectedFacesFlow: MutableSharedFlow<List<DetectedFace>> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val analyzer by lazy(LazyThreadSafetyMode.NONE) {
        MlKitAnalyzer(
            listOf(detector),
            ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
            Dispatchers.Default.asExecutor(),
        ) { result ->
            result.getValue(detector)?.let { results ->
                handleFaceDetectionResults(results)
            }
        }
    }

    private val faceDetectorOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .build()

    private val detector = FaceDetection.getClient(faceDetectorOptions)

    override fun release() {
        Timber.i("Releasing FaceDetectionAnalyzerWrapper")
        detector.close()
    }

    private fun handleFaceDetectionResults(results: List<Face>) {
        Timber.d("Detected faces: ${results.size}")
        val detectedFaces = results.map { face ->
            DetectedFace(face.boundingBox)
        }

        detectedFacesFlow.tryEmit(detectedFaces)
    }
}
