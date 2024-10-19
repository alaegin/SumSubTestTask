package com.example.sumsubtesttask.presentation.camera

import androidx.lifecycle.ViewModel
import com.example.sumsubtesttask.domain.face_detection.FaceDetectionAnalyzerWrapper
import com.example.sumsubtesttask.util.camera.CameraManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val faceDetectionAnalyzerWrapper: FaceDetectionAnalyzerWrapper,
) : ContainerHost<CameraViewState, CameraSideEffect>, ViewModel() {

    override val container = container<CameraViewState, CameraSideEffect>(CameraViewState()) {
        observeDetectedFaces()
    }

    override fun onCleared() {
        Timber.d("onCleared")
        super.onCleared()
        faceDetectionAnalyzerWrapper.release()
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

    fun onCameraCapabilitiesReceived(capabilities: CameraManager.Capabilities) {
        intent {
            Timber.d("onCameraCapabilitiesReceived, capabilities: $capabilities")

            reduce {
                state.copy(
                    isFrontCameraSupported = capabilities.hasFrontCamera,
                    isBackCameraSupported = capabilities.hasBackCamera,
                )
            }
        }
    }

    fun onGrantPermissionsClicked() {
        intent {
            Timber.d("onGrantPermissionsClicked")

            postSideEffect(CameraSideEffect.OpenAppSystemSettings)
        }
    }

    fun onToggleLensClicked() {
        intent {
            Timber.d("onToggleLensClicked")

            reduce {
                state.copy(cameraFacingFront = !state.cameraFacingFront)
            }

            postSideEffect(CameraSideEffect.ToggleLens(state.cameraFacingFront))
        }
    }

    private suspend fun handlePermissionsStatus(isGranted: Boolean) = subIntent {
        Timber.d("handlePermissionsStatus, isGranted: $isGranted")

        if (isGranted) {
            postSideEffect(CameraSideEffect.InitCamera(faceDetectionAnalyzerWrapper.analyzer))
        }

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

    private suspend fun Syntax<CameraViewState, CameraSideEffect>.observeDetectedFaces() {
        repeatOnSubscription {
            faceDetectionAnalyzerWrapper
                .detectedFacesFlow
                .sample(50.milliseconds)
                .onEach { Timber.v("${it.size} faces detected") }
                .collect { faces ->
                    reduce { state.copy(detectedFaces = faces) }
                }
        }
    }
}
