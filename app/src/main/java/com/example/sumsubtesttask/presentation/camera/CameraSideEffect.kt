package com.example.sumsubtesttask.presentation.camera

sealed interface CameraSideEffect {

    /**
     * Requests permissions for the camera.
     */
    data object RequestPermissions : CameraSideEffect

    /**
     * Opens the system settings of the application.
     */
    data object OpenAppSystemSettings : CameraSideEffect
}
