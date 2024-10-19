package com.example.sumsubtesttask.presentation.camera

import com.example.sumsubtesttask.domain.face_detection.model.DetectedFace

data class CameraViewState(
    val screenState: ScreenState = ScreenState.INIT,
    val detectedFaces: List<DetectedFace> = emptyList(),
) {
    enum class ScreenState {
        INIT,
        PERMISSIONS_REQUEST,
        CAMERA_PREVIEW
    }
}
