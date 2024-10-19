package com.example.sumsubtesttask.domain.face_detection

import androidx.camera.core.ImageAnalysis
import com.example.sumsubtesttask.domain.face_detection.model.DetectedFace
import kotlinx.coroutines.flow.SharedFlow

/**
 * Wrapper for Face Detection Analyzer
 *
 * It provides a flow of detected faces and gives an access to underlying ImageAnalysis.Analyzer
 */
interface FaceDetectionAnalyzerWrapper {

    val detectedFacesFlow: SharedFlow<List<DetectedFace>>
    val analyzer: ImageAnalysis.Analyzer

    /**
     * Releases resources and closes the analyzer
     */
    fun release()
}
