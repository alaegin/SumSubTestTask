package com.example.sumsubtesttask.presentation.camera

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ContainerHost<CameraViewState, CameraSideEffect>, ViewModel() {

    override val container = container<CameraViewState, CameraSideEffect>(CameraViewState())

    override fun onCleared() {
        Timber.d("onCleared")
        super.onCleared()
        container.cancel()
    }

    fun onScreenCreated() {
        intent {
            Timber.d("onScreenCreated")

            postSideEffect(CameraSideEffect.RequestPermissions)
        }
    }

    fun onScreenResumed(isPermissionGranted: Boolean) {
        intent {
            Timber.d("onScreenResumed, isPermissionGranted: $isPermissionGranted")

            handlePermissionsStatus(isPermissionGranted)
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        intent {
            Timber.d("onPermissionResult, isGranted: $isGranted")
            handlePermissionsStatus(isGranted)
        }
    }

    fun onGrantPermissionsClicked() {
        intent {
            Timber.d("onGrantPermissionsClicked")

            postSideEffect(CameraSideEffect.OpenAppSystemSettings)
        }
    }

    private suspend fun handlePermissionsStatus(isGranted: Boolean) = subIntent {
        Timber.d("handlePermissionsStatus, isGranted: $isGranted")
        reduce {
            state.copy(
                screenState = if (isGranted) {
                    CameraViewState.ScreenState.CAMERA_PREVIEW
                } else {
                    CameraViewState.ScreenState.PERMISSIONS_REQUEST
                },
            )
        }
    }
}
