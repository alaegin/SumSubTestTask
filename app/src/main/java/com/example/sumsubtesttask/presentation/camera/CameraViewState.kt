package com.example.sumsubtesttask.presentation.camera

import com.example.sumsubtesttask.domain.face_detection.model.DetectedFace

data class CameraViewState(
    val screenState: ScreenState = ScreenState.INIT,
    val detectedFaces: List<DetectedFace> = emptyList(),
    val isFrontCameraSupported: Boolean = false,
    val isBackCameraSupported: Boolean = false,
    val cameraFacingFront: Boolean = true,
) {

    val isCameraLensToggleVisible: Boolean
        get() = isFrontCameraSupported && isBackCameraSupported && screenState == ScreenState.CAMERA_PREVIEW

    enum class ScreenState {
        INIT,
        PERMISSIONS_REQUEST,
        CAMERA_PREVIEW
    }
}
