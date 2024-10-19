package com.example.sumsubtesttask.presentation.camera

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sumsubtesttask.R
import com.example.sumsubtesttask.databinding.FragmentCameraBinding
import com.example.sumsubtesttask.util.extension.isPermissionGranted
import com.example.sumsubtesttask.util.extension.openAppSystemSettings
import dagger.hilt.EntryPoint
import timber.log.Timber

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@EntryPoint
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

        if (checkPermissionGranted()) {
            onPermissionsGranted()
        } else {
            requestPermissions()
        }
    }

    override fun onResume() {
        super.onResume()

        if (checkPermissionGranted()) {
            onPermissionsGranted()
        }
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(CAMERA_PERMISSION)
    }

    private fun handlePermissionsRequestResult(isGranted: Boolean) {
        if (isGranted) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }

    private fun onPermissionsGranted() {
        Timber.d("Permissions granted")
        // TODO Notify ViewModel

        viewBinding.layoutPermissionsNotGranted.root.isGone = true

        // TODO
    }

    private fun onPermissionsDenied() {
        Timber.d("Permissions denied")
        // TODO Notify ViewModel

        viewBinding.layoutPermissionsNotGranted.apply {
            root.isGone = false
            // TODO Notify ViewModel
            buttonGrant.setOnClickListener { requireContext().openAppSystemSettings() }
        }
    }

    private fun checkPermissionGranted(): Boolean {
        return requireContext().isPermissionGranted(CAMERA_PERMISSION)
    }
}
