package com.example.sumsubtesttask.presentation.camera

import androidx.camera.core.ImageAnalysis

sealed interface CameraSideEffect {

    /**
     * Requests permissions for the camera.
     */
    data object RequestPermissions : CameraSideEffect

    /**
     * Opens the system settings of the application.
     */
    data object OpenAppSystemSettings : CameraSideEffect

    /**
     * Opens the camera.
     */
    data class InitCamera(val analyzer: ImageAnalysis.Analyzer) : CameraSideEffect
}
