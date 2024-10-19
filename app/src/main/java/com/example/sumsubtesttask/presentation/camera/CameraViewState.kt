package com.example.sumsubtesttask.presentation.camera

data class CameraViewState(
    val screenState: ScreenState = ScreenState.INIT,
) {
    enum class ScreenState {
        INIT,
        PERMISSIONS_REQUEST,
        CAMERA_PREVIEW
    }
}
