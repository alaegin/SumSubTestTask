package com.example.sumsubtesttask.presentation.camera

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sumsubtesttask.R
import com.example.sumsubtesttask.databinding.FragmentCameraBinding
import com.example.sumsubtesttask.util.extension.isPermissionGranted
import com.example.sumsubtesttask.util.extension.openAppSystemSettings
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@AndroidEntryPoint
class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val viewModel: CameraViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentCameraBinding::bind)

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        handlePermissionsRequestResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onScreenCreated()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.observe(lifecycleOwner = viewLifecycleOwner, state = ::render, sideEffect = ::handleSideEffect)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onScreenResumed(isPermissionGranted = checkPermissionGranted())
    }

    private fun initViews() {
        viewBinding.layoutPermissionsNotGranted.buttonGrant.setOnClickListener {
            viewModel.onGrantPermissionsClicked()
        }
    }

    private fun render(state: CameraViewState) {
        viewBinding.layoutPermissionsNotGranted.root.isVisible =
            state.screenState == CameraViewState.ScreenState.PERMISSIONS_REQUEST

        viewBinding.viewCameraPreview.isVisible =
            state.screenState == CameraViewState.ScreenState.CAMERA_PREVIEW
    }

    private fun handleSideEffect(sideEffect: CameraSideEffect) {
        when (sideEffect) {
            CameraSideEffect.OpenAppSystemSettings -> openAppSystemSettings()
            CameraSideEffect.RequestPermissions -> requestPermissions()
        }
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(CAMERA_PERMISSION)
    }

    private fun openAppSystemSettings() {
        requireContext().openAppSystemSettings()
    }

    private fun handlePermissionsRequestResult(isGranted: Boolean) {
        viewModel.onPermissionResult(isGranted)
    }

    private fun checkPermissionGranted(): Boolean {
        return requireContext().isPermissionGranted(CAMERA_PERMISSION)
    }
}
